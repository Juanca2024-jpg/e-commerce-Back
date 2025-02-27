package co.com.seti.usecase.order;

import co.com.seti.model.audit.ActionType;
import co.com.seti.model.audit.Audit;
import co.com.seti.model.audit.gateways.AuditGateway;
import co.com.seti.model.common.exception.InsufficientStock;
import co.com.seti.model.discount.Discount;
import co.com.seti.model.order.Order;
import co.com.seti.model.order.OrderDetail;
import co.com.seti.model.order.OrderStatus;
import co.com.seti.model.order.OrderType;
import co.com.seti.model.order.gateways.OrderDetailGateway;
import co.com.seti.model.order.gateways.OrderGateway;
import co.com.seti.model.product.Product;
import co.com.seti.model.user.User;
import co.com.seti.model.user.gateways.UserGateway;
import co.com.seti.usecase.discount.DiscountUseCase;
import co.com.seti.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class OrderUseCase {
    private final OrderGateway orderGateway;
    private final ProductUseCase productUseCase;
    private final DiscountUseCase discountUseCase;
    private final AuditGateway auditGateway;
    private final UserGateway userRepository;
    private final OrderDetailGateway itemRepository;

    public Order save(Order order, Long userId) throws Exception {

        validateUser(order.getUser().getId());

        initializeOrder(order);

        order.setOrderType(OrderType.NORMAL);

        Order savedOrder = orderGateway.save(order);

        processOrderDetails(savedOrder, userId);

        applyDiscount(savedOrder);

        applyLoyaltyDiscount(savedOrder, order.getUser().getId());

        finalizeOrder(savedOrder);

        auditOrderCreation(savedOrder, userId);

        return orderGateway.save(savedOrder);
    }

    private void validateUser(Long userId) throws Exception {
        if (userRepository.findById(userId) == null) {
            throw new Exception("El usuario no existe");
        }
    }

    private void initializeOrder(Order order) {
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
    }

    private void processOrderDetails(Order savedOrder, Long userId) throws Exception {
        for (OrderDetail orderItem : savedOrder.getOrderDetails()) {
            Product product = getProduct(orderItem.getProduct().getId());

            validateStock(product, orderItem.getQuantity());
            updateProductInventory(product, orderItem.getQuantity(), userId);

            orderItem.setOrderId(savedOrder.getId());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setSubtotal(orderItem.getQuantity() * product.getPrice());
            OrderDetail savedItem = itemRepository.save(orderItem);

            updateOrderTotal(savedOrder, product.getPrice(), savedItem.getQuantity());
        }
    }

    private Product getProduct(Long productId) throws Exception {
        return productUseCase.findById(productId);
    }

    private void validateStock(Product product, int quantity) {
        if (product.getInventory().getAvailableQuantity() < quantity) {
            throw new InsufficientStock("No cuenta con la cantidad suficiente del producto: " + product.getName());
        }
    }

    private void updateProductInventory(Product product, int quantity, Long userId) throws Exception {
        product.getInventory().setAvailableQuantity(product.getInventory().getAvailableQuantity() - quantity);
        productUseCase.updateProduct(product, userId);
    }

    private void updateOrderTotal(Order order, double price, int quantity) {
        order.setTotalAmount(order.getTotalAmount() + (price * quantity));
    }

    private void applyDiscount(Order order) {
        order.setDiscountedTotal(0.0);
        order.setAppliedDiscount(0.0);
        discountUseCase.getActiveTimeRangeDiscount()
                .ifPresent(discount -> {
                    double discountedTotal = order.getTotalAmount() * discount.getPercentage();
                    order.setDiscountedTotal(discountedTotal);
                    order.setAppliedDiscount(discount.getPercentage());
                });
    }

    private void applyLoyaltyDiscount(Order order, Long userId) {
        if (isFrequentCustomer(userId)) {
            double loyaltyDiscount = order.getTotalAmount() * 0.1;
            order.setDiscountedTotal(order.getDiscountedTotal() + loyaltyDiscount);
            order.setAppliedDiscount(order.getAppliedDiscount() + 0.1);
        }
    }

    private boolean isFrequentCustomer(Long userId) {
        return userRepository.getOrderCountByUser(userId) > 10;
    }

    private void finalizeOrder(Order order) {
        double finalPrice = order.getTotalAmount() - order.getDiscountedTotal();
        order.setTotalPrice(finalPrice);
    }

    private void auditOrderCreation(Order order, Long userId) {
        auditGateway.save(Audit.builder()
                .user(User.builder().id(userId).build())
                .action(ActionType.CREATE)
                .actionDate(LocalDateTime.now())
                .affectedRecordId(order.getId())
                .affectedTable("order")
                .build());
    }

    public Order generateRandomOrder(Long userId) throws Exception {
        validateUser(userId);

        Order randomOrder = new Order();
        randomOrder.setUser(userRepository.findById(userId));
        initializeOrder(randomOrder);

        randomOrder.setOrderType(OrderType.RANDOM);

        List<Product> products = productUseCase.findAll();
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            Product product = products.get(random.nextInt(products.size()));
            int quantity = random.nextInt(5) + 1;

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);

            randomOrder.getOrderDetails().add(orderDetail);
        }

        return save(randomOrder, userId);
    }

    public Order findById(Long id) throws Exception {
        Order order = orderGateway.findById(id);
        if(order == null) {
            throw new Exception("La orden con el id:  " + id + " no existe");
        }
        return order;
    }

    public Order updateStatus(Long id, OrderStatus status, Long userId) throws Exception {
        Order orderToUpdate = orderGateway.findById(id);
        if (orderToUpdate == null) {
            throw new Exception("La orden con el id: " + id + " no existe");
        }
        orderToUpdate.setStatus(status);

        Order saveOrder = orderGateway.update(orderToUpdate);

        auditGateway.save(Audit
                .builder()
                .user(User.builder().id(userId).build())
                .action(ActionType.UPDATE)
                .actionDate(LocalDateTime.now())
                .affectedRecordId(saveOrder.getId())
                .affectedTable("order")
                .build());

        return saveOrder;
    }
}

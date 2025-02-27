package co.com.seti.usecase.order;

import co.com.seti.model.audit.Audit;
import co.com.seti.model.audit.gateways.AuditGateway;
import co.com.seti.model.order.Order;
import co.com.seti.model.order.OrderDetail;
import co.com.seti.model.order.OrderStatus;
import co.com.seti.model.order.gateways.OrderDetailGateway;
import co.com.seti.model.order.gateways.OrderGateway;
import co.com.seti.model.product.Inventory;
import co.com.seti.model.product.Product;
import co.com.seti.model.user.User;
import co.com.seti.usecase.product.ProductUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderUseCaseTest {
    @Mock
    private OrderGateway orderGateway;
    @Mock
    private ProductUseCase productUseCase;
    @Mock
    private OrderDetailGateway itemRepository;
    @Mock
    private AuditGateway auditGateway;

    @InjectMocks
    private OrderUseCase orderUseCase;


    @Test
    public void save() throws Exception {
        OrderDetail orderDetail = OrderDetail.builder()
                .orderId(1L)
                .id(1L)
                .product(Product.builder().id(1L).build())
                .quantity(20000)
                .build();
        Order order = Order.builder()
                .orderDetails(List.of(orderDetail))
                .user(User.builder().id(1L).build())
                .id(1L)
                .status(OrderStatus.PENDING)
                .build();
        Product product = Product.builder()
                .price(20000.0)
                .id(1L)
                .name("product")
                .active(true)
                .build();
        when(productUseCase.findById(any())).thenReturn(product);
        when(productUseCase.updateProduct(any(Product.class),1L)).thenReturn(product);
        when(orderGateway.save(any())).thenReturn(order);
        when(itemRepository.save(any())).thenReturn(orderDetail);
        when(auditGateway.save(any(Audit.class))).thenReturn(Audit.builder().build());
        Order orderSaved = orderUseCase.save(order, 1L);
        Assertions.assertEquals(order.getId(), orderSaved.getId());
        Assertions.assertEquals(order.getOrderDetails().size(), orderSaved.getOrderDetails().size());
        Assertions.assertEquals(9, product.getInventory().getAvailableQuantity());
    }
    @Test
    public void saveInsufficientStock() throws Exception {
        OrderDetail orderDetail = OrderDetail.builder()
                .orderId(1L)
                .id(1L)
                .product(Product.builder().id(1L).build())
                .quantity(20000)
                .build();
        Order order = Order.builder()
                .orderDetails(List.of(orderDetail))
                .user(User.builder().id(1L).build())
                .id(1L)
                .status(OrderStatus.PENDING)
                .build();
        Product product = Product.builder()
                .price(20000.0)
                .id(1L)
                .name("product")
                .active(true)
                .inventory(Inventory.builder().availableQuantity(0).build())
                .build();
        when(productUseCase.findById(any())).thenReturn(product);
        Assertions.assertThrows(RuntimeException.class, ()->{
           orderUseCase.save(order, 1L);
        });
    }
    @Test
    public void saveError() throws Exception {
        OrderDetail orderDetail = OrderDetail.builder()
                .orderId(1L)
                .id(1L)
                .product(Product.builder().id(1L).build())
                .quantity(20000)
                .build();
        Order order = Order.builder()
                .orderDetails(List.of(orderDetail))
                .user(User.builder().id(1L).build())
                .id(1L)
                .status(OrderStatus.PENDING)
                .build();
        when(productUseCase.findById(any())).thenThrow(new Exception("mock Error"));
        Assertions.assertThrows(Exception.class, ()->{
            orderUseCase.save(order, 1L);
        });
    }

    @Test
    public void updateStatus() throws Exception {
        Order order = Order.builder()
                .status(OrderStatus.PENDING)
                .id(1L)
                .user(User.builder().id(2L).build())
                .orderDetails(List.of())
                .build();
        when(orderGateway.findById(any(Long.class))).thenReturn(order);
        when(orderGateway.update(any())).thenReturn(order);
        when(auditGateway.save(any(Audit.class))).thenReturn(Audit.builder().build());

        Order orderUpdated = orderUseCase.updateStatus(1L, OrderStatus.valueOf("PENDING"), 1L);
        Assertions.assertEquals(OrderStatus.PENDING, orderUpdated.getStatus());
    }

    @Test
    public void updateStatusError() throws Exception {
        Order order = null;
        when(orderGateway.findById(any(Long.class))).thenReturn(order);
        when(auditGateway.save(any(Audit.class))).thenReturn(Audit.builder().build());

        Assertions.assertThrows(Exception.class, ()->{
            orderUseCase.updateStatus(1L, OrderStatus.valueOf("PENDING"), 1L);
        });
    }
    @Test
    public void findById() throws Exception {
        Order order = Order.builder()
                .status(OrderStatus.PENDING)
                .id(1L)
                .user(User.builder().id(1L).build())
                .orderDetails(List.of())
                .build();
        when(orderGateway.findById(any())).thenReturn(order);
        Order getOrder = orderUseCase.findById(1L);
        Assertions.assertEquals(1L, getOrder.getId());
        Assertions.assertEquals(2L, getOrder.getUser().getId());
    }
    @Test
    public void findByIdError() throws Exception {
        Order order = null;
        when(orderGateway.findById(any())).thenReturn(order);
        Assertions.assertThrows(Exception.class, ()->{
            orderUseCase.findById(1L);
        });
    }
}
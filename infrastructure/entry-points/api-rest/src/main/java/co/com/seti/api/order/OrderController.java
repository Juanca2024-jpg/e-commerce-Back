package co.com.seti.api.order;

import co.com.seti.api.order.requests.CreateOrderRequest;
import co.com.seti.api.order.requests.UpdateOrderRequest;
import co.com.seti.helper.JwtUtil;
import co.com.seti.model.order.Order;
import co.com.seti.model.order.OrderDetail;
import co.com.seti.model.order.OrderStatus;
import co.com.seti.model.order.OrderType;
import co.com.seti.model.product.Product;
import co.com.seti.model.user.User;
import co.com.seti.usecase.order.OrderUseCase;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderUseCase orderUseCase;
    private final JwtUtil jwtUtilGateway;

    @PostMapping(path = "/orders")
    public ResponseEntity<Order> newOrder(
            @Valid @RequestBody CreateOrderRequest order,
            @RequestHeader("Authorization") String authorizationHeader
    ) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "").trim();
        Long userId = jwtUtilGateway.getUserId(token);

        Order order1 = Order.builder()
                .orderDetails(order.getItems().stream().map(
                        orderItemRequest -> OrderDetail.builder()
                                .product(Product.builder().id(orderItemRequest.getProductId()).build())
                                .quantity(orderItemRequest.getQuantity())
                                .build()
                ).toList())
                .user(User.builder().id(userId).build())
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = null;

        if(order.getOrderType().equals(OrderType.NORMAL))
            savedOrder = orderUseCase.save(order1, userId);

        if(order.getOrderType().equals(OrderType.RANDOM))
            savedOrder = orderUseCase.generateRandomOrder(userId);

        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id")  Long id) throws Exception {
        return ResponseEntity.ok(orderUseCase.findById(id));
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<Order> updateOrder(@Valid @RequestBody UpdateOrderRequest order,
                                             @PathVariable("id")  Long id,
                                             @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "").trim();
        Long userId = jwtUtilGateway.getUserId(token);

        return ResponseEntity.ok(orderUseCase.updateStatus(id, order.getState(), userId));
    }
}

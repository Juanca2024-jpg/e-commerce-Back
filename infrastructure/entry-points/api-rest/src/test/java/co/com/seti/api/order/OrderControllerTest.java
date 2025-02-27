package co.com.seti.api.order;

import co.com.seti.api.order.requests.CreateOrderRequest;
import co.com.seti.api.order.requests.UpdateOrderRequest;
import co.com.seti.helper.JwtUtil;
import co.com.seti.model.order.Order;
import co.com.seti.model.order.OrderDetail;
import co.com.seti.model.order.OrderStatus;
import co.com.seti.model.product.Product;
import co.com.seti.model.user.User;
import co.com.seti.usecase.order.OrderUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderUseCase orderUseCase;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private OrderController orderController;
    @Test
    public void newOrder() throws Exception {
        OrderDetail orderDetail = OrderDetail.builder()
                .product(Product.builder().id(1L).build())
                .quantity(20000)
                .build();

        Order order = Order.builder()
                .orderDetails(List.of(orderDetail))
                .user(User.builder().id(1L).build())
                .id(1L)
                .status(OrderStatus.PENDING)
                .build();
        when(orderUseCase.save(any(),anyLong())).thenReturn(order);
        when(jwtUtil.getUserId(anyString())).thenReturn(1L);
        CreateOrderRequest request = new CreateOrderRequest();
        ResponseEntity<Order> orderResponseEntity = orderController.newOrder(request,"token");
        Assertions.assertEquals(200, orderResponseEntity.getStatusCode().value());
    }

    @Test
    public void getOrder() throws Exception {
        OrderDetail orderDetail = OrderDetail.builder()
                .orderId(1L)
                .id(1L)
                .quantity(20000)
                .build();
        Order order = Order.builder()
                .orderDetails(List.of(orderDetail))
                .id(1L)
                .status(OrderStatus.PENDING)
                .build();
        when(orderUseCase.findById(any())).thenReturn(order);
        ResponseEntity<Order> orderResponseEntity = orderController.getOrder(1L);
        Assertions.assertEquals(200, orderResponseEntity.getStatusCode().value());
    }

    @Test
    public void updateOrder() throws Exception {
        OrderDetail orderDetail = OrderDetail.builder()
                .orderId(1L)
                .id(1L)
                .quantity(20000)
                .build();
        Order order = Order.builder()
                .orderDetails(List.of(orderDetail))
                .id(1L)
                .status(OrderStatus.PENDING)
                .build();
        when(orderUseCase.updateStatus(any(), any(), anyLong())).thenReturn(order);
        when(jwtUtil.getUserId(anyString())).thenReturn(1L);
        UpdateOrderRequest request = new UpdateOrderRequest();
        request.setState(OrderStatus.valueOf("CANCELLED"));
        ResponseEntity<Order> orderResponseEntity = orderController.updateOrder(request, 1L, "token");
        Assertions.assertEquals(200, orderResponseEntity.getStatusCode().value());
    }
}
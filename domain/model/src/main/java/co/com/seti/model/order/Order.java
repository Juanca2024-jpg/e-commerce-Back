package co.com.seti.model.order;

import co.com.seti.model.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Order {

    private Long id;

    private User user;

    private OrderStatus status;

    private Double totalAmount;

    private LocalDateTime orderDate;

    private Double totalPrice;

    private Double discountedTotal;

    private OrderType orderType;

    private Double appliedDiscount;

    private List<OrderDetail> orderDetails;
}
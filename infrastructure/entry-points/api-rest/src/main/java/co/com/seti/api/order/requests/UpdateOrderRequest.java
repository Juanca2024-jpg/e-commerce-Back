package co.com.seti.api.order.requests;

import co.com.seti.model.order.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderRequest {
    OrderStatus state;
}

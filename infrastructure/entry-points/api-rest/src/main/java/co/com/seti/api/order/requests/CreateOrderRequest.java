package co.com.seti.api.order.requests;

import co.com.seti.model.order.OrderType;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    private OrderType orderType;

    private List<OrderItemRequest> items;

}

package co.com.seti.model.order.gateways;

import co.com.seti.model.order.OrderDetail;

public interface OrderDetailGateway {
    OrderDetail save(OrderDetail orderDetail);
}

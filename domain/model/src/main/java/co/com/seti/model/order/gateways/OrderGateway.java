package co.com.seti.model.order.gateways;

import co.com.seti.model.order.Order;
import co.com.seti.model.user.User;

import java.util.List;

public interface OrderGateway {
    Order save(Order order);

    Order findById(Long id);

    List<Order> findAll();

    Order update(Order order);

    List<User> getTop5FrequentCustomers();
}

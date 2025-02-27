package co.com.seti.jpa.order;

import co.com.seti.jpa.helper.AdapterOperations;
import co.com.seti.jpa.order.entities.OrderEntity;
import co.com.seti.jpa.user.entities.UserEntity;
import co.com.seti.model.order.Order;
import co.com.seti.model.order.gateways.OrderGateway;
import co.com.seti.model.user.Role;
import co.com.seti.model.user.User;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderGatewayAdapter extends AdapterOperations<Order, OrderEntity, Long, OrderDataRepository>
 implements OrderGateway
{

    public OrderGatewayAdapter(OrderDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Order.class));
    }

    @Override
    public Order update(Order order) {
        return this.save(order);
    }

    @Override
    public List<User> getTop5FrequentCustomers() {
        Pageable pageable = PageRequest.of(0, 5);
        return repository.findTop5FrequentCustomers(pageable).getContent()
                .stream().map(userEntity ->
                    User.builder()
                            .id(userEntity.getId())
                            .name(userEntity.getName())
                            .email(userEntity.getEmail())
                            .role(Role.builder()
                                    .id(userEntity.getRole().getId())
                                    .name(userEntity.getRole().getName())
                                    .build())
                            .build()
                ).toList();
    }

}

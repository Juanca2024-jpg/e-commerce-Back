package co.com.seti.jpa.order;

import co.com.seti.jpa.helper.AdapterOperations;
import co.com.seti.jpa.order.entities.OrderDetailEntity;
import co.com.seti.model.order.OrderDetail;
import co.com.seti.model.order.gateways.OrderDetailGateway;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDetailGatewayAdapter extends AdapterOperations<OrderDetail, OrderDetailEntity, Long, OrderDetailDataRepository>
        implements OrderDetailGateway {

    public OrderDetailGatewayAdapter(OrderDetailDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, OrderDetail.class));
    }

}

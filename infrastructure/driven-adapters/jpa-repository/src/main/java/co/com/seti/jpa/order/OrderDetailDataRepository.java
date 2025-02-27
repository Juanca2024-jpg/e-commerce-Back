package co.com.seti.jpa.order;

import co.com.seti.jpa.order.entities.OrderDetailEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface OrderDetailDataRepository extends CrudRepository<OrderDetailEntity, Long>, QueryByExampleExecutor<OrderDetailEntity> {
}

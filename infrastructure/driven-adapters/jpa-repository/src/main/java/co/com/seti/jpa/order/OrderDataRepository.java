package co.com.seti.jpa.order;

import co.com.seti.jpa.order.entities.OrderEntity;
import co.com.seti.jpa.user.entities.UserEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

public interface OrderDataRepository extends CrudRepository<OrderEntity, Long>, QueryByExampleExecutor<OrderEntity> {
    @NonNull
    @Query(
            """
            select p from OrderEntity p
            """
    )
    List<OrderEntity> findAll();

    @Query("SELECT u FROM UserEntity u " +
            "WHERE u.id IN (SELECT o.user.id FROM OrderEntity o " +
            "GROUP BY o.user.id " +
            "ORDER BY COUNT(o.id) DESC)")
    Page<UserEntity> findTop5FrequentCustomers(Pageable pageable);

}

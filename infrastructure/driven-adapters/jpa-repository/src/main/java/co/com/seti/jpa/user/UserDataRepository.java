package co.com.seti.jpa.user;

import co.com.seti.jpa.user.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface UserDataRepository extends CrudRepository<UserEntity, Long>, QueryByExampleExecutor<UserEntity> {

    Boolean existsByIdAndActiveTrue(Long id);
    Boolean existsByEmailAndActiveTrue(String email);
    UserEntity findByEmailAndActiveTrue(String email);

    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.user.id = :userId")
    int getOrderCountByUser(@Param("userId") Long userId);
}

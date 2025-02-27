package co.com.seti.jpa.user;

import co.com.seti.jpa.user.entities.RoleEntity;
import co.com.seti.jpa.user.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface RoleDataRepository extends CrudRepository<RoleEntity, Long>, QueryByExampleExecutor<RoleEntity> {
    RoleEntity findByName(String name);
}

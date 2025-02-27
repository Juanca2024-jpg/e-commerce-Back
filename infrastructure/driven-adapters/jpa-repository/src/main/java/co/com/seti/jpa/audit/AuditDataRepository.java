package co.com.seti.jpa.audit;

import co.com.seti.jpa.audit.entities.AuditEntity;
import co.com.seti.jpa.product.entities.ProductEntity;
import co.com.seti.jpa.user.entities.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;

public interface AuditDataRepository extends CrudRepository<AuditEntity, Long>, QueryByExampleExecutor<AuditEntity> {

    @NonNull
    @Query(
            """
            select a from AuditEntity a
            """
    )
    List<AuditEntity> findAll();
}

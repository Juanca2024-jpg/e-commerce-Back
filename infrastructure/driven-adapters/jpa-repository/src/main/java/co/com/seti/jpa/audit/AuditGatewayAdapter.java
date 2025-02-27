package co.com.seti.jpa.audit;

import co.com.seti.jpa.audit.entities.AuditEntity;
import co.com.seti.jpa.helper.AdapterOperations;
import co.com.seti.jpa.user.UserDataRepository;
import co.com.seti.jpa.user.entities.UserEntity;
import co.com.seti.model.audit.Audit;
import co.com.seti.model.audit.gateways.AuditGateway;
import co.com.seti.model.product.Product;
import co.com.seti.model.user.User;
import co.com.seti.model.user.gateways.UserGateway;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuditGatewayAdapter extends AdapterOperations<Audit, AuditEntity, Long, AuditDataRepository>
        implements AuditGateway {

    public AuditGatewayAdapter(AuditDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Audit.class));
    }

    @Override
    public Audit findById(Long id) {
        return null;
    }

    @Override
    public List<Audit> findAll() {
        return toList(repository.findAll());
    }
}

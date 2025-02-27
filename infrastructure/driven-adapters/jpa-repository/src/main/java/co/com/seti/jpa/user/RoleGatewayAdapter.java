package co.com.seti.jpa.user;

import co.com.seti.jpa.helper.AdapterOperations;
import co.com.seti.jpa.user.entities.RoleEntity;
import co.com.seti.jpa.user.entities.UserEntity;
import co.com.seti.model.user.Role;
import co.com.seti.model.user.User;
import co.com.seti.model.user.gateways.RoleGateway;
import co.com.seti.model.user.gateways.UserGateway;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class RoleGatewayAdapter extends AdapterOperations<Role, RoleEntity, Long, RoleDataRepository>
        implements RoleGateway {

    public RoleGatewayAdapter(RoleDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
    }

    @Override
    public Role getRole(String roleName) {
        return toEntity(repository.findByName(roleName));
    }
}

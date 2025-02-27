package co.com.seti.jpa.user;

import co.com.seti.jpa.helper.AdapterOperations;
import co.com.seti.jpa.user.entities.UserEntity;
import co.com.seti.model.user.User;
import co.com.seti.model.user.gateways.UserGateway;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserGatewayAdapter extends AdapterOperations<User, UserEntity, Long, UserDataRepository>
        implements UserGateway {

    public UserGatewayAdapter(UserDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public User register(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return toEntity(repository.save(toData(user)));
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsByIdAndActiveTrue(id);
    }

    @Override
    public User update(User userUpdate) {

        User user = toEntity(repository.findById(userUpdate.getId()).orElse(null));

        if( userUpdate.getPassword() != null && !passwordEncoder.matches(user.getPassword(), userUpdate.getPassword()) ) {
            user.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        }

        return this.save(user);
    }

    @Override
    public User validatePassword(String email, String password) throws Exception {

        User user = toEntity(repository.findByEmailAndActiveTrue(email));

        if( !passwordEncoder.matches(password, user.getPassword()) ) {
            throw new Exception("La contraseña es incorrecta");
        }

        return user;
    }

    @Override
    public boolean existByEmail(String email) {
        return repository.existsByEmailAndActiveTrue(email);
    }

    @Override
    public int getOrderCountByUser(Long userId) {
        return repository.getOrderCountByUser(userId);
    }
}

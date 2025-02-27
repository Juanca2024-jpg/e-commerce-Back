package co.com.seti.model.user.gateways;

import co.com.seti.model.user.User;

public interface UserGateway {

    User save(User user);

    User findById(Long id);

    User register(User user);

    boolean existsById(Long id);

    User update(User user);

    User validatePassword(String email, String password) throws Exception;

    boolean existByEmail(String email);

    int getOrderCountByUser(Long userId);
}

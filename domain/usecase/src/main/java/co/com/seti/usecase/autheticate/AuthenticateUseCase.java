package co.com.seti.usecase.autheticate;

import co.com.seti.model.common.exception.ErrorException;
import co.com.seti.model.common.exception.codeError;
import co.com.seti.model.common.jwt.JwtUtilGateway;
import co.com.seti.model.user.User;
import co.com.seti.model.user.gateways.UserGateway;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public class AuthenticateUseCase {

    private final UserGateway userGateway;

    private final JwtUtilGateway jwtUtil;

    public User save(User user) throws ErrorException {
        boolean existingUser = this.userGateway.existByEmail(user.getEmail());

        if (existingUser) {
            throw new ErrorException("El correo ya existe", codeError.FOUND.getCode());
        }

        return this.userGateway.register(user);
    }

    public String login(String email, String password) throws Exception {
        boolean existingUser = this.userGateway.existByEmail(email);

        if (!existingUser)
            throw new ErrorException("El usuario no fue encontrado", codeError.NOT_FOUND.getCode());

        User user = this.userGateway.validatePassword(email, password);

        Map<String, Object> map = new HashMap<>();

        map.put("rol", List.of(user.getRole().getName()));
        map.put("id", user.getId());

        return jwtUtil.generateToken(user.getEmail(), map);
    }

}

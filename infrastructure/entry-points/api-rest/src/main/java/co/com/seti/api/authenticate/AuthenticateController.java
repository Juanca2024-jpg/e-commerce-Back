package co.com.seti.api.authenticate;

import co.com.seti.api.authenticate.requests.LoginRequest;
import co.com.seti.api.authenticate.requests.RegisterRequest;
import co.com.seti.api.authenticate.response.LoginResponse;
import co.com.seti.api.user.response.UserDetail;
import co.com.seti.model.user.Role;
import co.com.seti.model.user.User;
import co.com.seti.usecase.autheticate.AuthenticateUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AuthenticateController {

    private final AuthenticateUseCase authenticateUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request) throws Exception {

        String token = authenticateUseCase.login(request.email(), request.password());

        return ResponseEntity.ok(LoginResponse.builder().token(token).build());
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDetail> registerUser(@Valid @RequestBody RegisterRequest request) throws Exception {

        User user = authenticateUseCase.save(
                User
                        .builder()
                        .name(request.name())
                        .email(request.email())
                        .password(request.password())
                        .role(Role.builder().id(2L).build())
                        .build()
        );

        return ResponseEntity.ok(
                UserDetail.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build()
        );
    }


}

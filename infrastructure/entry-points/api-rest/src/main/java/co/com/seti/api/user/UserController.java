package co.com.seti.api.user;

import co.com.seti.api.user.requests.CreateUserRequest;
import co.com.seti.api.user.requests.UpdateUserRequest;
import co.com.seti.api.user.response.UserDetail;
import co.com.seti.helper.JwtUtil;
import co.com.seti.model.common.MessageDTO;
import co.com.seti.model.user.Role;
import co.com.seti.model.user.RoleType;
import co.com.seti.model.user.User;
import co.com.seti.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor

@SecurityRequirement(name = "bearerAuth")

@PreAuthorize("hasAuthority('Admin')")
public class UserController {

    private final UserUseCase userUseCase; 

    private final JwtUtil jwtUtil;

    @GetMapping("/{id}")
    public ResponseEntity<UserDetail> getUser(@PathVariable("id") Long id) throws Exception {

        User user = userUseCase.findById(id);

        return ResponseEntity.ok(
                UserDetail.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(RoleType.valueOf(user.getRole().getName()))
                        .build()
        );
    }

    @PostMapping()
    public ResponseEntity<UserDetail> addUser(@Valid @RequestBody CreateUserRequest request,
                                              @RequestHeader("Authorization") String authorizationHeader) throws Exception {

        String token = authorizationHeader.replace("Bearer ", "").trim();

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .role(Role.builder().name(String.valueOf(request.role())).build())
                .build();

        Long userId = jwtUtil.getUserId(token);
        User savedUser = userUseCase.save(user, userId);
        return ResponseEntity.ok(
                UserDetail.builder()
                        .id(savedUser.getId())
                        .name(savedUser.getName())
                        .email(savedUser.getEmail())
                        .role(RoleType.valueOf(savedUser.getRole().getName()))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetail> updateUser(@Valid @RequestBody UpdateUserRequest request
            ,@PathVariable("id") Long id,
                                                 @RequestHeader("Authorization") String authorizationHeader) throws Exception {

        String token = authorizationHeader.replace("Bearer ", "").trim();

        User user = User.builder()
                .id(id)
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        Long userId = jwtUtil.getUserId(token);

        User savedUser = userUseCase.updateUser(user, userId);
        return ResponseEntity.ok(
                UserDetail.builder()
                        .id(savedUser.getId())
                        .name(savedUser.getName())
                        .email(savedUser.getEmail())
                        .role(RoleType.valueOf(savedUser.getRole().getName()))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO<String>> deleteUser(@PathVariable("id") Long id,
                                                            @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "").trim();
        Long userId = jwtUtil.getUserId(token);

        return ResponseEntity.ok().body(new MessageDTO<>(userUseCase.deleteUser(id, userId)));
    }
}

package co.com.seti.model.user;

import co.com.seti.model.order.Order;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private Long id;

    private String name;

    private String email;

    private String password;

    private Role role;

    private LocalDateTime createdAt;

    private Boolean active;
}

package co.com.seti.api.user.response;

import co.com.seti.model.user.RoleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetail {

    private Long id;
    private String name;
    private String email;
    private RoleType role;
}

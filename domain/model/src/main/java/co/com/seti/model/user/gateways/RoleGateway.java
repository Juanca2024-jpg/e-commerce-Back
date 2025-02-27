package co.com.seti.model.user.gateways;

import co.com.seti.model.user.Role;

public interface RoleGateway {
    Role getRole(String roleName);
}

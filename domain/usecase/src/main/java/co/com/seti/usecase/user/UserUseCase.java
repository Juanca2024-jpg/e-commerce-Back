package co.com.seti.usecase.user;

import co.com.seti.model.audit.ActionType;
import co.com.seti.model.audit.Audit;
import co.com.seti.model.audit.gateways.AuditGateway;
import co.com.seti.model.user.Role;
import co.com.seti.model.user.User;
import co.com.seti.model.user.gateways.RoleGateway;
import co.com.seti.model.user.gateways.UserGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class UserUseCase {
    
    private final UserGateway userRepository;

    private final RoleGateway roleGateway;

    private final AuditGateway auditGateway;

    public User save(User user, Long userId) throws Exception {

        boolean exists = userRepository.existByEmail(user.getEmail());

        if (exists)
            throw new Exception("El email " + user.getEmail() + " ya existe.");

        Role role = roleGateway.getRole(user.getRole().getName());

        if (role == null)
            throw new Exception("El rol " + user.getRole().getName() + " no existe.");

        user.setCreatedAt(LocalDateTime.now());
        user.setRole(role);

        User savedUser = userRepository.register(user);

        auditGateway.save(Audit
                .builder()
                .user(User.builder().id(userId).build())
                .action(ActionType.DELETE)
                .actionDate(LocalDateTime.now())
                .affectedRecordId(savedUser.getId())
                .affectedTable("user")
                .build());

        return savedUser;
    }

    public User findById(Long id) throws Exception {
        boolean existingUser = this.userRepository.existsById(id);

        if (!existingUser) {
            throw new Exception("El usuario con el id " + id + " no existe.");
        }
        return this.userRepository.findById(id);
    }

    public User updateUser(User userUpdate, Long userId) throws Exception {
        boolean existingUser = this.userRepository.existsById(userUpdate.getId());

        if (!existingUser) {
            throw new Exception("El usuario con el id " + userUpdate.getId() + " no existe.");
        }

        User user = userRepository.findById(userUpdate.getId());

        if(!user.getEmail().equals(userUpdate.getEmail()) &&
                userRepository.existByEmail(userUpdate.getEmail())) {
            throw new Exception("El correo " + userUpdate.getEmail() + " ya existe.");
        }

        user.setName(userUpdate.getName() == null ? user.getName() : userUpdate.getName());
        user.setEmail(userUpdate.getEmail() == null ? user.getName() : userUpdate.getEmail());
        user.setPassword(userUpdate.getPassword() == null ? user.getPassword() : userUpdate.getPassword());

        User updateUser = userRepository.update(user);

        auditGateway.save(Audit
                .builder()
                .user(User.builder().id(userId).build())
                .action(ActionType.UPDATE)
                .actionDate(LocalDateTime.now())
                .affectedRecordId(updateUser.getId())
                .affectedTable("user")
                .build());

        return updateUser;
    }

    public String deleteUser(Long id, Long userId) throws Exception {
        boolean existingUser = this.userRepository.existsById(id);

        if (!existingUser) {
            throw new Exception("El usuario con el id " + id + " no existe");
        }
        User user = this.userRepository.findById(id);

        user.setActive(false);

        User updateUser = this.userRepository.save(user);

        auditGateway.save(Audit
                .builder()
                .user(User.builder().id(userId).build())
                .action(ActionType.DELETE)
                .actionDate(LocalDateTime.now())
                .affectedRecordId(updateUser.getId())
                .affectedTable("user")
                .build());

        return "El usuario con el id " + id +" fue eliminado correctamente.";
    }
}

package co.com.seti.jpa.user.entities;

import co.com.seti.jpa.order.entities.OrderEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;

    private String password;

    @ManyToOne
    private RoleEntity role;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    private Boolean active = Boolean.TRUE;

}

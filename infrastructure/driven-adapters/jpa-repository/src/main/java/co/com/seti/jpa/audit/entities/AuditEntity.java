package co.com.seti.jpa.audit.entities;

import co.com.seti.jpa.user.entities.UserEntity;
import co.com.seti.model.audit.ActionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private ActionType action;

    private String affectedTable;

    private Long affectedRecordId;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime actionDate;

}

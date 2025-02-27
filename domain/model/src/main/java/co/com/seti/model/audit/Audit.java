package co.com.seti.model.audit;

import co.com.seti.model.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Audit {

    private Long id;

    private User user;

    private ActionType action;

    private String affectedTable;

    private Long affectedRecordId;

    private LocalDateTime actionDate;
}

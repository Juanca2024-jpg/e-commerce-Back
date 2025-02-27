package co.com.seti.model.discount;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Discount {

    private Long id;

    private String description;

    private Double percentage;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private DiscountType type;

    private Boolean active;
}

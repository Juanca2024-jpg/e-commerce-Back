package co.com.seti.model.product;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductFilters {
    private Long id;
    private String name;
    private String description;
    private Double minPrice;
    private Double maxPrice;
    private Boolean active;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

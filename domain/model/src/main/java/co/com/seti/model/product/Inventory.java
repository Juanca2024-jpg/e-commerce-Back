package co.com.seti.model.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Inventory {

    private Long id;

    private Integer availableQuantity;
}

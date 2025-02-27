package co.com.seti.model.product;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Boolean active;

    private Inventory inventory;

    private LocalDateTime createdAt;

}

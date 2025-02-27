package co.com.seti.api.product.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateProductRequest {

    private String name;

    @Positive(message = "El precio no puede ser negativo")
    private Double price;

    private String description;

    @Positive(message = "La cantidad disponible no puede ser negativo")
    private Integer availableQuantity;
}

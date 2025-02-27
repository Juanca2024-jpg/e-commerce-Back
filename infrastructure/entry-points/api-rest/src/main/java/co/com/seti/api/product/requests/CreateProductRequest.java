package co.com.seti.api.product.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateProductRequest {
    @NotEmpty(message = "El nombre no puede estar vacio.")
    private String name;

    @Positive(message = "El precio no puede ser negativo.")
    private Double price;

    private String description;

    @Positive(message = "La cantidad disponible no puede ser negativo.")
    private Integer availableQuantity;
}

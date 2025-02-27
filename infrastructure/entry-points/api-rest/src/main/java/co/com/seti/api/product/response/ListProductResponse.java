package co.com.seti.api.product.response;

import co.com.seti.model.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ListProductResponse {

    private boolean applyDiscount;

    private Double percentage;

    private List<Product> products;

}

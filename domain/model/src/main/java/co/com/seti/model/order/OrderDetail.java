package co.com.seti.model.order;

import co.com.seti.model.product.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderDetail {

    private Long id;

    private Long orderId;

    private Product product;

    private Integer quantity;

    private Double unitPrice;

    private Double subtotal;
}
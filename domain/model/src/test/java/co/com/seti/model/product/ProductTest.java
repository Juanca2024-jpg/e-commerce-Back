package co.com.seti.model.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductTest {
    @Test
    public void testProduct() {
        Product product = Product.builder()
                .productId(1L)
                .isActive(true)
                .name("coca-cola")
                .price(4000.00)
                .build();
        Assertions.assertEquals(1L, product.getProductId());
        Assertions.assertEquals(true, product.getIsActive());
        Assertions.assertEquals("coca-cola", product.getName());
        Assertions.assertEquals(4000.00, product.getPrice());
    }

}
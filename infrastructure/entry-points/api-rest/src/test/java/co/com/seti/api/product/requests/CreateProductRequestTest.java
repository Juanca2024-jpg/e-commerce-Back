package co.com.seti.api.product.requests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreateProductRequestTest {
    @Test
    public void testCreateProductRequest() {
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setName("coca-cola");
        createProductRequest.setPrice(4000.00);
        Assertions.assertEquals("coca-cola",createProductRequest.getName());
        Assertions.assertEquals(4000.00,createProductRequest.getPrice());
    }

}
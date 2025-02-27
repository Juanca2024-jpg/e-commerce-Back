package co.com.seti.api.product;

import co.com.seti.api.product.requests.CreateProductRequest;
import co.com.seti.api.product.requests.UpdateProductRequest;
import co.com.seti.helper.JwtUtil;
import co.com.seti.model.common.MessageDTO;
import co.com.seti.model.product.Product;
import co.com.seti.usecase.product.ProductUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductControllerTest {
    private ProductController productController;
    private ProductUseCase productUseCase;
    private JwtUtil jwtUtil;
    @BeforeEach
    public void setUp() {
        productUseCase = mock(ProductUseCase.class);
        jwtUtil = mock(JwtUtil.class);
        productController = new ProductController(productUseCase, jwtUtil);
    }

    /*
    @Test
    public void getProductAllTest(){
        when(productUseCase.findAll()).then(invocation -> Arrays.asList());
        ResponseEntity<List<Product>> products = productController.getProductAll();
        Assertions.assertEquals(HttpStatus.OK, products.getStatusCode());
    }
     */

    @Test
    public void addProductTest() throws Exception{
        CreateProductRequest request = new CreateProductRequest();
        request.setName("coca-cola");
        request.setPrice(4000.00);
        when(jwtUtil.getUserId(anyString())).thenReturn(1L);
        when(productUseCase.save(getProduct(request.getName(), request.getPrice(), null), 1L))
                .thenReturn((getProduct(request.getName(), request.getPrice(), null)));
        ResponseEntity<Product> products = productController.addProduct(request, "token");
        Assertions.assertEquals(HttpStatus.OK, products.getStatusCode());
    }
    @Test
    public void updateProductTest() throws Exception{
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("coca-cola");
        request.setPrice(4000.00);

        Product product = getProduct(request.getName(), request.getPrice(), 1L);

        when(jwtUtil.getUserId(anyString())).thenReturn(1L);
        when(productUseCase.save(product, 1L)).thenReturn(product);
        ResponseEntity<Product> products = productController.updateProduct(request,1L, "token");
        Assertions.assertEquals(HttpStatus.OK, products.getStatusCode());
    }
    @Test
    public void deleteProductTest() throws Exception{
        when(jwtUtil.getUserId(anyString())).thenReturn(1L);
        when(productUseCase.deleteProduct(1L, 1L)).thenReturn("delete success");
        ResponseEntity<MessageDTO<String>> response = productController.deleteProduct(1L, "token");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private Product getProduct(String name, Double price, Long id){
        return Product.builder().name(name).price(price).id(id).build();
    }
}
package co.com.seti.usecase.product;

import co.com.seti.model.audit.gateways.AuditGateway;
import co.com.seti.model.product.Product;
import co.com.seti.model.product.gateways.ProductGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductUseCaseTest {
    private ProductGateway productGateway;
    private ProductUseCase productUseCase;

    @BeforeEach
    public void setUp() {
        productGateway = mock(ProductGateway.class);
        AuditGateway auditGateway = mock(AuditGateway.class);
        productUseCase = new ProductUseCase(productGateway, auditGateway);
    }
    @Test
    public void saveTest(){
        Product product = Product.builder().build();
        when(productGateway.save(product)).thenReturn(product);
        Product savedProduct = productUseCase.save(product, 1L);
        Assertions.assertEquals(product, savedProduct);
    }
    @Test
    public void findByIdWhenProductDontExistsTest(){
        when(productGateway.existById(1L)).thenReturn(false);
        Assertions.assertThrows(Exception.class, () -> productUseCase.findById(1L));
    }
    @Test
    public void findByIdWhenProductExistsTest() throws Exception{
        when(productGateway.existById(1L)).thenReturn(true);
        when(productGateway.findById(1L)).thenReturn(Product.builder().name("coca-cola").build());
        Product product = productUseCase.findById(1L);
        Assertions.assertEquals("coca-cola", product.getName());
    }
    @Test
    public void findAllTest(){
        List<Product> products = List.of(Product.builder().name("coca-cola").build());
        when(productGateway.findAll()).thenReturn(products);
        List<Product> allProducts = productUseCase.findAll();
        Assertions.assertEquals(products, allProducts);
    }
    @Test
    public void updateProductWhenProductExistsTest() throws Exception{
        Product product = Product.builder()
                .name("coca-cola")
                .id(1L)
                .price(4000.00)
                .build();
        when(productGateway.findById(1L)).thenReturn(product);
        when(productGateway.existById(1L)).thenReturn(true);
        when(productGateway.update(product)).thenReturn(product);
        Product resultProduct = productUseCase.updateProduct(product, 1L);
        Assertions.assertEquals(product, resultProduct);
    }
    @Test
    public void updateProductWhenProductDontExistsTest() throws Exception{
        Product product = Product.builder()
                .name("coca-cola")
                .id(1L)
                .price(4000.00)
                .build();
        when(productGateway.existById(1L)).thenReturn(false);
        Assertions.assertThrows(Exception.class, () -> productUseCase.updateProduct(product, 1L));
    }
    @Test
    public void deleteProductWhenProductExistsTest() throws Exception{
        when(productGateway.existById(1L)).thenReturn(true);
        when(productGateway.findById(1L)).thenReturn(Product.builder().name("coca-cola").build());
        String productId = productUseCase.deleteProduct(1L, 1L);
        Assertions.assertEquals("El producto con el id " + 1L +" fue eliminado correctamente.", productId);
    }
    @Test
    public void deleteProductWhenProductDontExistsTest(){
        when(productGateway.existById(1L)).thenReturn(false);
        Assertions.assertThrows(Exception.class, () -> productUseCase.deleteProduct(1L, 1L));

    }
}
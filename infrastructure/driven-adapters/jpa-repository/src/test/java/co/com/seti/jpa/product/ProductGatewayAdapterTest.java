package co.com.seti.jpa.product;

import co.com.seti.jpa.product.entities.ProductEntity;
import co.com.seti.model.product.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivecommons.utils.ObjectMapperImp;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductGatewayAdapterTest {
    private ProductGatewayAdapter productRepositoryAdapter;
    private ProductDataRepository productDataRepository;
    @BeforeEach
    public void setUp() {
        productDataRepository = mock(ProductDataRepository.class);
        productRepositoryAdapter = new ProductGatewayAdapter(productDataRepository, new ObjectMapperImp());
    }
    @Test
    public void updateTest(){
        when(productDataRepository.save(Mockito.any(ProductEntity.class))).
                thenReturn(ProductEntity.builder().
                        name("producto").
                        price(3000.0).
                        active(true).
                        id(1L).build());

        Product product = productRepositoryAdapter.update(
                Product.builder().
                        name("producto").
                        price(3000.0).
                        active(true).
                      id(1L).build());

        Assertions.assertNotNull(product);
        Assertions.assertEquals("producto", product.getName());
    }
    @Test
    public void existByIdTest(){
        when(productRepositoryAdapter.existById(1L)).thenReturn(true);
        boolean exist = productRepositoryAdapter.existById(1L);
        Assertions.assertTrue(exist);
    }
}
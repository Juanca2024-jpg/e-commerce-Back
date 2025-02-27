package co.com.seti.model.product.gateways;

import co.com.seti.model.common.PageRequests;
import co.com.seti.model.product.Product;
import co.com.seti.model.product.ProductFilters;

import java.util.List;

public interface ProductGateway {
    Product save(Product product);

    Product findById(Long id);

    List<Product> findByFilters(ProductFilters filters, PageRequests pageRequests);

    List<Product> findAll();

    Product update(Product product);

    boolean existById(Long id);

    List<Product> getTop5BestSellingProducts();
}

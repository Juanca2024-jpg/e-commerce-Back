package co.com.seti.jpa.product;

import co.com.seti.jpa.helper.AdapterOperations;
import co.com.seti.jpa.product.entities.ProductEntity;
import co.com.seti.model.common.PageRequests;
import co.com.seti.model.product.Product;
import co.com.seti.model.product.ProductFilters;
import co.com.seti.model.product.gateways.ProductGateway;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductGatewayAdapter extends AdapterOperations<Product, ProductEntity, Long, ProductDataRepository>
 implements ProductGateway
{

    public ProductGatewayAdapter(ProductDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Product.class));
    }

    @Override
    public Product update(Product product) {
        return this.save(product);
    }

    @Override
    public boolean existById(Long id) {
        return this.repository.existsById(id);
    }

    @Override
    public List<Product> getTop5BestSellingProducts() {
        Pageable pageable = PageRequest.of(0, 5);
        return repository.findTop5BestSellingProducts(pageable).getContent()
                .stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public List<Product> findByFilters(ProductFilters filters, PageRequests pageRequests) {
        Pageable pageable = PageRequest.of(
                pageRequests.getPage(),
                pageRequests.getSize(),
                pageRequests.getSort() != null && !pageRequests.getSort().isEmpty()
                        ? Sort.by(pageRequests.getSort())
                        : Sort.unsorted()
        );

        return repository.findByFilters(
                        filters.getId(),
                        filters.getName(),
                        filters.getDescription(),
                        filters.getMinPrice(),
                        filters.getMaxPrice(),
                        filters.getActive(),
                        filters.getStartDate(),
                        filters.getEndDate(),
                        pageable)
                .map(this::toEntity)
                .toList();
    }

    public List<Product> findAll(){
        return toList(repository.findAll());
    }
}

package co.com.seti.usecase.product;

import co.com.seti.model.audit.ActionType;
import co.com.seti.model.audit.Audit;
import co.com.seti.model.audit.gateways.AuditGateway;
import co.com.seti.model.common.PageRequests;
import co.com.seti.model.product.Product;
import co.com.seti.model.product.ProductFilters;
import co.com.seti.model.product.gateways.ProductGateway;
import co.com.seti.model.user.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductGateway productGateway;

    private final AuditGateway auditGateway;

    public Product save(Product product, Long userId) {
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());

        Product savedProduct = productGateway.save(product);

        auditGateway.save(Audit
                .builder()
                .user(User.builder().id(userId).build())
                .action(ActionType.DELETE)
                .actionDate(LocalDateTime.now())
                .affectedRecordId(savedProduct.getId())
                .affectedTable("product")
                .build());

        return savedProduct;
    }

    public Product findById(Long id) throws Exception {
        boolean existingProduct = this.productGateway.existById(id);

        if (!existingProduct) {
            throw new Exception("El producto con el id " + id + " no existe.");
        }
        return this.productGateway.findById(id);
    }

    public List<Product> findByFilters(ProductFilters filters, PageRequests pageRequests){
        return this.productGateway.findByFilters(filters, pageRequests);
    }

    public List<Product> findAll() {
        return this.productGateway.findAll();
    }

    public Product updateProduct(Product productUpdate, Long userId) throws Exception {
        boolean existingProduct = this.productGateway.existById(productUpdate.getId());

        if (!existingProduct) {
            throw new Exception("El producto con el id " + productUpdate.getId() + " no existe.");
        }

        Product product = productGateway.findById(productUpdate.getId());
        product.setName(productUpdate.getName());
        product.setPrice(productUpdate.getPrice());
        product.setDescription(productUpdate.getDescription());
        product.getInventory().setAvailableQuantity(productUpdate.getInventory().getAvailableQuantity());

        Product updateProduct = productGateway.update(product);
        auditGateway.save(Audit
                        .builder()
                        .user(User.builder().id(userId).build())
                        .action(ActionType.UPDATE)
                        .actionDate(LocalDateTime.now())
                        .affectedRecordId(updateProduct.getId())
                        .affectedTable("product")
                        .build());

        return updateProduct;
    }

    public String deleteProduct(Long id, Long userId) throws Exception {
        boolean existingProduct = this.productGateway.existById(id);

        if (!existingProduct) {
            throw new Exception("El producto con el id " + id + " no existe");
        }
        Product product = this.productGateway.findById(id);
        product.setActive(false);
        Product updateProduct = this.productGateway.update(product);

        auditGateway.save(Audit
                .builder()
                .user(User.builder().id(userId).build())
                .action(ActionType.DELETE)
                .actionDate(LocalDateTime.now())
                .affectedRecordId(updateProduct.getId())
                .affectedTable("product")
                .build());

        return "El producto con el id " + id +" fue eliminado correctamente.";
    }
}

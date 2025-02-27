package co.com.seti.jpa.product;

import co.com.seti.jpa.product.entities.ProductEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductDataRepository extends CrudRepository<ProductEntity, Long>, QueryByExampleExecutor<ProductEntity> {
    @NonNull
    @Query(
            """
            select p from ProductEntity p where p.id = :id and p.active = true
            """
    )
    Optional<ProductEntity> findById(@NonNull @Param("id") Long id);

    @NonNull
    @Query(
            """
            select p from ProductEntity p where p.active = true
            """
    )
    List<ProductEntity> findAll();

    @Query("SELECT p FROM ProductEntity p WHERE " +
            "(p.id = COALESCE(:id, p.id)) AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', COALESCE(:name, ''), '%'))) AND " +
            "(LOWER(p.description) LIKE LOWER(CONCAT('%', COALESCE(:description, ''), '%'))) AND " +
            "(p.price >= COALESCE(:minPrice, 0)) AND " +
            "(p.price <= COALESCE(:maxPrice, p.price)) AND " +
            "(p.active = COALESCE(:active, p.active)) AND " +
            "(p.createdAt >= COALESCE(:startDate, p.createdAt)) AND " +
            "(p.createdAt <= COALESCE(:endDate, p.createdAt))")
    Page<ProductEntity> findByFilters(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("description") String description,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("active") Boolean active,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);



    @Query("SELECT p FROM OrderDetailEntity oi " +
            "JOIN oi.product p " +
            "GROUP BY p.id " +
            "ORDER BY SUM(oi.quantity) DESC")
    Page<ProductEntity> findTop5BestSellingProducts(Pageable pageable);
}

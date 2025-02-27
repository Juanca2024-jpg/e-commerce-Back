package co.com.seti.jpa.discount;

import co.com.seti.jpa.discount.entities.DiscountEntity;
import co.com.seti.model.discount.DiscountType;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DiscountDataRepository extends CrudRepository<DiscountEntity, Long>, QueryByExampleExecutor<DiscountEntity> {

    @Query("SELECT d FROM DiscountEntity d WHERE d.type = :type AND :date BETWEEN d.startDate AND d.endDate")
    List<DiscountEntity> findActiveDiscountsByType(
            @Param("date") LocalDateTime date,
            @Param("type") DiscountType type);

    @Query("SELECT d FROM DiscountEntity d WHERE d.type = :type AND " +
            "((:startDate BETWEEN d.startDate AND d.endDate) OR " +
            "(:endDate BETWEEN d.startDate AND d.endDate) OR " +
            "(d.startDate BETWEEN :startDate AND :endDate))")
    List<DiscountEntity> findOverlappingTimeRangeDiscounts(
            @Param("type") DiscountType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}

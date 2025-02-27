package co.com.seti.model.discount.gateways;

import co.com.seti.model.discount.Discount;
import co.com.seti.model.discount.DiscountType;

import java.time.LocalDateTime;
import java.util.List;

public interface DiscountGateway {
    Discount save(Discount discount);

    List<Discount> findActiveDiscounts(LocalDateTime date, DiscountType type);

    List<Discount> findOverlappingTimeRangeDiscounts(DiscountType type,
                                                     LocalDateTime startDate,
                                                     LocalDateTime endDate);

    Discount update(Discount discount);

}

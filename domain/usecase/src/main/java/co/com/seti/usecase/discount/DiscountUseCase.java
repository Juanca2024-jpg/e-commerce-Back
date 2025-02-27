package co.com.seti.usecase.discount;

import co.com.seti.model.discount.Discount;
import co.com.seti.model.discount.DiscountType;
import co.com.seti.model.discount.gateways.DiscountGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DiscountUseCase {

    private final DiscountGateway discountGateway;

    public Optional<Discount> getActiveTimeRangeDiscount() {
        return  discountGateway.findActiveDiscounts(
                LocalDateTime.now(), DiscountType.TIME_RANGE)
                .stream().findFirst();
    }

    public Discount addDiscount(Discount discount) {
        if (discount.getType() == DiscountType.TIME_RANGE) {
            List<Discount> overlapping = discountGateway.findOverlappingTimeRangeDiscounts(
                    DiscountType.TIME_RANGE, discount.getStartDate(), discount.getEndDate());

            if (!overlapping.isEmpty()) {
                throw new IllegalArgumentException("Ya existe un descuento TIME_RANGE para el rango de fechas especificado.");
            }
        }
        return discountGateway.save(discount);
    }

}

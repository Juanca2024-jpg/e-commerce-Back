package co.com.seti.api.discount;

import co.com.seti.model.common.exception.ErrorException;
import co.com.seti.model.discount.Discount;
import co.com.seti.usecase.discount.DiscountUseCase;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/discount")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DiscountController {

    private final DiscountUseCase discountUseCase;

    @GetMapping("/active-timerange")
    public ResponseEntity<?> getActiveTimeRangeDiscount() throws ErrorException {

        return discountUseCase.getActiveTimeRangeDiscount()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ErrorException("No hay descuentos TIME_RANGE activos para la fecha especificada.", 404));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDiscount(@RequestBody Discount discount) {
        try {
            Discount savedDiscount = discountUseCase.addDiscount(discount);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDiscount);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }


}

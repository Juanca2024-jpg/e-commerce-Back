package co.com.seti.api.report;

import co.com.seti.api.user.response.UserDetail;
import co.com.seti.model.product.Product;
import co.com.seti.model.user.RoleType;
import co.com.seti.model.user.User;
import co.com.seti.usecase.product.ProductUseCase;
import co.com.seti.usecase.report.ReportUseCase;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")

@PreAuthorize("hasAuthority('Admin')")
public class ReportController {

    private final ReportUseCase reportUseCase;

    private final ProductUseCase productUseCase;

    @GetMapping("/all-products")
    public ResponseEntity<List<Product>> getProductAll() {
        return ResponseEntity.ok(productUseCase.findAll());
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<Product>> getTop5BestSellingProducts() {
        List<Product> topProducts = reportUseCase.getTop5BestSellingProducts();
        return ResponseEntity.ok(topProducts);
    }

    @GetMapping("/top-users")
    public ResponseEntity<List<UserDetail>> getTop5FrequentCustomers() {
        List<User> topCustomers = reportUseCase.getTop5FrequentCustomers();
        return ResponseEntity.ok(topCustomers
                .stream().map( user ->
                        UserDetail.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .email(user.getEmail())
                                .role(RoleType.valueOf(user.getRole().getName()))
                                .build()
                ).toList());
    }
}


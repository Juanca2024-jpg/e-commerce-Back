package co.com.seti.api.product;

import co.com.seti.api.product.requests.CreateProductRequest;
import co.com.seti.api.product.requests.UpdateProductRequest;
import co.com.seti.api.product.response.ListProductResponse;
import co.com.seti.helper.JwtUtil;
import co.com.seti.model.common.MessageDTO;
import co.com.seti.model.common.PageRequests;
import co.com.seti.model.discount.Discount;
import co.com.seti.model.product.Inventory;
import co.com.seti.model.product.Product;
import co.com.seti.model.product.ProductFilters;
import co.com.seti.usecase.discount.DiscountUseCase;
import co.com.seti.usecase.product.ProductUseCase;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/product", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")

@PreAuthorize("hasAuthority('Admin')")
public class ProductController {

    private final ProductUseCase productUseCase;

    private final DiscountUseCase discountUseCase;

    private final JwtUtil jwtUtil;

    @GetMapping("")
    public ResponseEntity<?> getProducts(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false) String sort) {

        ProductFilters filters = ProductFilters.builder()
                .id(id)
                .name(name)
                .description(description)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .active(true)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        PageRequests pageRequests = PageRequests.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .build();

        Discount discount = discountUseCase.getActiveTimeRangeDiscount().orElse(null);

        if (discount != null){
            return ResponseEntity.ok(
                    ListProductResponse.builder().products(
                    productUseCase.findByFilters(filters, pageRequests)
                    .stream().peek((product) ->
                                    product.setPrice(product.getPrice()* discount.getPercentage()))
                            .toList())
                            .applyDiscount(true)
                            .percentage(discount.getPercentage())
                            .build()
            );
        }

        return ResponseEntity.ok(
                ListProductResponse.builder()
                        .products(productUseCase.findByFilters(filters, pageRequests))
                        .applyDiscount(false)
                        .percentage(0.0)
                        .build()
        );
    }

    @PostMapping()
    public ResponseEntity<Product> addProduct(@Valid @RequestBody CreateProductRequest request,
                                              @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "").trim();

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .inventory(Inventory.builder().availableQuantity(request.getAvailableQuantity()).build())
                .build();

        Long userId = jwtUtil.getUserId(token);
        Product savedProduct = productUseCase.save(product, userId);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody UpdateProductRequest request
                                                ,@PathVariable("id") Long id,
                                                 @RequestHeader("Authorization") String authorizationHeader) throws Exception {

        String token = authorizationHeader.replace("Bearer ", "").trim();

        Product product = Product.builder()
                .id(id)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .inventory(Inventory.builder().availableQuantity(request.getAvailableQuantity()).build())
                .build();

        Long userId = jwtUtil.getUserId(token);

        Product savedProduct = productUseCase.updateProduct(product, userId);
        return ResponseEntity.ok(savedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO<String>> deleteProduct(@PathVariable("id") Long id,
                                                            @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "").trim();
        Long userId = jwtUtil.getUserId(token);

        return ResponseEntity.ok().body(new MessageDTO<>(productUseCase.deleteProduct(id, userId)));
    }
}

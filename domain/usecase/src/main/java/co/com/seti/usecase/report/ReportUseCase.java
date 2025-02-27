package co.com.seti.usecase.report;

import co.com.seti.model.order.gateways.OrderGateway;
import co.com.seti.model.product.Product;
import co.com.seti.model.product.gateways.ProductGateway;
import co.com.seti.model.user.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ReportUseCase {

    private final ProductGateway productGateway;

    private final OrderGateway orderGateway;

    public List<Product> getTop5BestSellingProducts() {
        return productGateway.getTop5BestSellingProducts();
    }

    public List<User> getTop5FrequentCustomers() {
        return orderGateway.getTop5FrequentCustomers();
    }
}

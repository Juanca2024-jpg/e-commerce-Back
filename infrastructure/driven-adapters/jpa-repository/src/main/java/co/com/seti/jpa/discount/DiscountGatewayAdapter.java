package co.com.seti.jpa.discount;

import co.com.seti.jpa.discount.entities.DiscountEntity;
import co.com.seti.jpa.helper.AdapterOperations;
import co.com.seti.jpa.user.UserDataRepository;
import co.com.seti.jpa.user.entities.UserEntity;
import co.com.seti.model.discount.Discount;
import co.com.seti.model.discount.DiscountType;
import co.com.seti.model.discount.gateways.DiscountGateway;
import co.com.seti.model.discount.Discount;
import co.com.seti.model.user.User;
import co.com.seti.model.user.gateways.UserGateway;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DiscountGatewayAdapter extends AdapterOperations<Discount, DiscountEntity, Long, DiscountDataRepository>
        implements DiscountGateway {

    public DiscountGatewayAdapter(DiscountDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Discount.class));
    }

    @Override
    public List<Discount> findActiveDiscounts(LocalDateTime date, DiscountType type) {
        return repository.findActiveDiscountsByType(date, type)
                .stream().map(this::toEntity).toList();
    }

    @Override
    public List<Discount> findOverlappingTimeRangeDiscounts(DiscountType type, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findOverlappingTimeRangeDiscounts(
                type, startDate, endDate).stream().map(this::toEntity).toList();
    }

    @Override
    public Discount update(Discount discount) {
        return this.save(discount);
    }

    public List<Discount> findAll() {
        return toList(repository.findAll());
    }
}

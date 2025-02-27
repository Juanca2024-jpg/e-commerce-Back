package co.com.seti.jpa.order.entities;

import co.com.seti.jpa.user.entities.UserEntity;
import co.com.seti.model.order.OrderStatus;
import co.com.seti.model.order.OrderType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private UserEntity user;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private OrderStatus status = OrderStatus.PENDING;

        private double totalAmount;

        @Temporal(TemporalType.TIMESTAMP)
        private LocalDateTime orderDate;

        private Double totalPrice;

        private Double discountedTotal = 0.0;

        private OrderType orderType;

        private Double appliedDiscount = 0.0;

        @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL)
        private Collection<OrderDetailEntity> orderDetails;

}

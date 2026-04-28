package com.farmconnect.farmconnect_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    private Product product;

    private Integer quantity;
    private BigDecimal price;

    // This method fixes the Stream map error
    public BigDecimal getSubtotal() {
        if (this.price == null || this.quantity == null) return BigDecimal.ZERO;
        return this.price.multiply(BigDecimal.valueOf(this.quantity));
    }
}

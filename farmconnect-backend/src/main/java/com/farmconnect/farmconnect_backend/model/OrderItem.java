package com.farmconnect.farmconnect_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Order relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnoreProperties({"items"})
    private Order order;
    
    // Product relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({})
    private Product product;
    
    @Column(nullable = false)
    private Integer quantity;
    
    // Use BigDecimal
    @Column(nullable = false)
    private BigDecimal pricePerUnit;
    
    @Column(nullable = false)
    private BigDecimal subtotal;
    
    // Auto-calculate subtotal safely
    @PrePersist
    @PreUpdate
    protected void calculateSubtotal() {
        if (this.pricePerUnit != null && this.quantity != null) {
            this.subtotal = this.pricePerUnit.multiply(BigDecimal.valueOf(this.quantity));
        }
    }
}
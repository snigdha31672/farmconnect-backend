package com.farmconnect.farmconnect_backend.config;

import com.farmconnect.farmconnect_backend.model.*;
import com.farmconnect.farmconnect_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return; // already seeded

        // ── Users ──
        // Changed .name() to .fullName() and .role() to String or Enum as per your model
        User farmer = userRepository.save(User.builder()
                .username("farmer_john")
                .fullName("John Farmer")
                .email("farmer@farm.com")
                .password(passwordEncoder.encode("123"))
                .role("FARMER") 
                .build());

        User buyer = userRepository.save(User.builder()
                .username("jane_buyer")
                .fullName("Jane Buyer")
                .email("buyer@email.com")
                .password(passwordEncoder.encode("123"))
                .role("BUYER")
                .build());

        userRepository.save(User.builder()
                .username("admin")
                .fullName("Admin User")
                .email("admin@farm.com")
                .password(passwordEncoder.encode("123"))
                .role("ADMIN")
                .build());

        // ── Products ──
        // Changed price to BigDecimal.valueOf() and quantity to stock
        List<Product> products = productRepository.saveAll(List.of(
            Product.builder().name("Organic Honey")
                .price(BigDecimal.valueOf(25.99)).unit("500g jar")
                .category("Organic Food").farmer(farmer).stock(50)
                .image("https://th.bing.com/th/id/OIP.QqEOu8JLMWQ-oIFi2G0XRwHaJ8?w=137&h=184&c=7&r=0&o=7&dpr=1.8&pid=1.7&rm=3")
                .description("Pure organic honey from local beehives").build(),
            
            Product.builder().name("Handmade Strawberry Jam")
                .price(BigDecimal.valueOf(8.99)).unit("300g jar")
                .category("Processed Foods").farmer(farmer).stock(100)
                .image("https://th.bing.com/th/id/OIP.Qv89ImIGz8rc59GbyA0I2wHaHa?w=195&h=195&c=7&r=0&o=7&dpr=1.8&pid=1.7&rm=3")
                .description("Homemade jam from fresh strawberries").build(),
            
            Product.builder().name("Extra Virgin Olive Oil")
                .price(BigDecimal.valueOf(35.99)).unit("1L bottle")
                .category("Processed Foods").farmer(farmer).stock(30)
                .image("https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=400")
                .description("Cold-pressed from organic olives").build()
        ));

        // ── Sample Orders ──
        // Changed total and price to BigDecimal.valueOf()
        Order o1 = Order.builder()
                .buyer(buyer)
                .total(BigDecimal.valueOf(34.98))
                .status("delivered")
                .date(LocalDate.of(2024, 1, 15))
                .farmer(farmer)
                .build();
        
        OrderItem oi1 = OrderItem.builder()
                .order(o1)
                .product(products.get(0))
                .quantity(1)
                .price(BigDecimal.valueOf(25.99))
                .build();
        
        OrderItem oi2 = OrderItem.builder()
                .order(o1)
                .product(products.get(1))
                .quantity(1)
                .price(BigDecimal.valueOf(8.99))
                .build();
        
        o1.setItems(List.of(oi1, oi2));
        orderRepository.save(o1);

        log.info("✅ Database seeded successfully");
    }
}

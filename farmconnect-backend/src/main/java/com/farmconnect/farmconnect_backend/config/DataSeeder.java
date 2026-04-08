package com.farmconnect.farmconnect_backend.config;

import com.farmconnect.farmconnect_backend.model.*;
import com.farmconnect.farmconnect_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
        User farmer = userRepository.save(User.builder()
                .name("John Farmer").email("farmer@farm.com")
                .password(passwordEncoder.encode("123"))
                .role(Role.FARMER).farmName("Green Valley Farm")
                .location("California, USA").rating(4.8).build());

        User buyer = userRepository.save(User.builder()
                .name("Jane Buyer").email("buyer@email.com")
                .password(passwordEncoder.encode("123"))
                .role(Role.BUYER).location("New York, USA").rating(0.0).build());

        userRepository.save(User.builder()
                .name("Admin").email("admin@farm.com")
                .password(passwordEncoder.encode("123"))
                .role(Role.ADMIN).rating(0.0).build());

        // ── Products ──
        List<Product> products = productRepository.saveAll(List.of(
            Product.builder().name("Organic Honey").price(25.99).unit("500g jar")
                .category("Organic Food").farmer(farmer).stock(50).rating(4.8).reviews(24)
                .image("https://th.bing.com/th/id/OIP.QqEOu8JLMWQ-oIFi2G0XRwHaJ8?w=137&h=184&c=7&r=0&o=7&dpr=1.8&pid=1.7&rm=3")
                .description("Pure organic honey from local beehives").build(),
            Product.builder().name("Handmade Strawberry Jam").price(8.99).unit("300g jar")
                .category("Processed Foods").farmer(farmer).stock(100).rating(4.6).reviews(18)
                .image("https://th.bing.com/th/id/OIP.Qv89ImIGz8rc59GbyA0I2wHaHa?w=195&h=195&c=7&r=0&o=7&dpr=1.8&pid=1.7&rm=3")
                .description("Homemade jam from fresh strawberries").build(),
            Product.builder().name("Extra Virgin Olive Oil").price(35.99).unit("1L bottle")
                .category("Processed Foods").farmer(farmer).stock(30).rating(4.9).reviews(32)
                .image("https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=400")
                .description("Cold-pressed from organic olives").build(),
            Product.builder().name("Artisan Bread").price(6.99).unit("Loaf")
                .category("Baked Goods").farmer(farmer).stock(25).rating(4.7).reviews(15)
                .image("https://images.unsplash.com/photo-1509440159596-0249088772ff?w=400")
                .description("Fresh baked daily with organic flour").build(),
            Product.builder().name("Organic Coffee Beans").price(18.99).unit("500g bag")
                .category("Beverages").farmer(farmer).stock(80).rating(4.5).reviews(42)
                .image("https://th.bing.com/th/id/OIP.3Vifsy9dH5bDpeXYxF_OnwHaEO?w=274&h=185&c=7&r=0&o=7&dpr=1.8&pid=1.7&rm=3")
                .description("Fair trade organic coffee").build(),
            Product.builder().name("Handmade Soap").price(12.99).unit("100g bar")
                .category("Handmade Goods").farmer(farmer).stock(200).rating(4.9).reviews(56)
                .image("https://tse4.mm.bing.net/th/id/OIP.WCgRuU_h6K8O19gKRNjeKAHaHa?w=1024&h=1024&rs=1&pid=ImgDetMain&o=7&rm=3")
                .description("Natural soap with essential oils").build()
        ));

        // ── Sample Orders ──
        Order o1 = Order.builder().buyer(buyer).total(34.98).status("delivered")
                .date(LocalDate.of(2024, 1, 15)).farmerId(farmer.getId()).build();
        OrderItem oi1 = OrderItem.builder().order(o1).product(products.get(0)).quantity(1).price(25.99).build();
        OrderItem oi2 = OrderItem.builder().order(o1).product(products.get(1)).quantity(1).price(8.99).build();
        o1.setItems(List.of(oi1, oi2));
        orderRepository.save(o1);

        Order o2 = Order.builder().buyer(buyer).total(35.99).status("shipped")
                .date(LocalDate.of(2024, 1, 18)).farmerId(farmer.getId()).build();
        OrderItem oi3 = OrderItem.builder().order(o2).product(products.get(2)).quantity(1).price(35.99).build();
        o2.setItems(List.of(oi3));
        orderRepository.save(o2);

        Order o3 = Order.builder().buyer(buyer).total(25.98).status("pending")
                .date(LocalDate.of(2024, 1, 20)).farmerId(farmer.getId()).build();
        OrderItem oi4 = OrderItem.builder().order(o3).product(products.get(3)).quantity(1).price(6.99).build();
        OrderItem oi5 = OrderItem.builder().order(o3).product(products.get(4)).quantity(1).price(18.99).build();
        o3.setItems(List.of(oi4, oi5));
        orderRepository.save(o3);

        log.info("✅ Database seeded: 3 users, 6 products, 3 orders");
    }
}

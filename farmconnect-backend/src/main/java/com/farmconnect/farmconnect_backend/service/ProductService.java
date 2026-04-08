package com.farmconnect.farmconnect_backend.service;

import com.farmconnect.farmconnect_backend.dto.ProductDto;
import com.farmconnect.farmconnect_backend.model.Product;
import com.farmconnect.farmconnect_backend.model.User;
import com.farmconnect.farmconnect_backend.repository.ProductRepository;
import com.farmconnect.farmconnect_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Page<Product> getAllProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    public Page<Product> getByCategory(String category, int page, int size) {
        return productRepository.findByCategory(category, PageRequest.of(page, size));
    }

    public Page<Product> search(String query, int page, int size) {
        return productRepository.search(query, PageRequest.of(page, size));
    }

    public Page<Product> getByFarmer(Long farmerId, int page, int size) {
        User farmer = userRepository.findById(farmerId).orElseThrow();
        return productRepository.findByFarmer(farmer, PageRequest.of(page, size));
    }

    public Product create(ProductDto dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User farmer = userRepository.findByEmail(email).orElseThrow();

        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .unit(dto.getUnit())
                .category(dto.getCategory())
                .image(dto.getImage())
                .stock(dto.getStock())
                .rating(0.0)
                .reviews(0)
                .farmer(farmer)
                .build();

        return productRepository.save(product);
    }

    public Product update(Long id, ProductDto dto) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setUnit(dto.getUnit());
        product.setCategory(dto.getCategory());
        if (dto.getImage() != null) product.setImage(dto.getImage());
        product.setStock(dto.getStock());
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}

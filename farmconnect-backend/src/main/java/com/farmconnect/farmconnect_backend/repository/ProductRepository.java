package com.farmconnect.farmconnect_backend.repository;

import com.farmconnect.farmconnect_backend.model.Product;
import com.farmconnect.farmconnect_backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByFarmer(User farmer, Pageable pageable);
    Page<Product> findByCategory(String category, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

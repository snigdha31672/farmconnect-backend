package com.farmconnect.farmconnect_backend.repository;

import com.farmconnect.farmconnect_backend.model.Product;
import com.farmconnect.farmconnect_backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(String category, Pageable pageable);
    

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%',:query,'%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%',:query,'%'))")
    Page<Product> search(String query, Pageable pageable);
}

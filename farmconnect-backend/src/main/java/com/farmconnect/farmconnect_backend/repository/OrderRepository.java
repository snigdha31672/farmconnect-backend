package com.farmconnect.farmconnect_backend.repository;

import com.farmconnect.farmconnect_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Buyer orders
    List<Order> findByBuyerId(Long buyerId);

    // Farmer orders (correct way)
    @Query("SELECT DISTINCT o FROM Order o JOIN o.items i JOIN i.product p WHERE p.farmer.id = :farmerId")
    List<Order> findOrdersByFarmerId(@Param("farmerId") Long farmerId);

    // Status filter
    List<Order> findByStatus(String status);

    // Sorted orders
    List<Order> findAllByOrderByDateDesc();
}
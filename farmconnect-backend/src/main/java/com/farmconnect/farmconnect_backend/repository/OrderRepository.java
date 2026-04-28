package com.farmconnect.farmconnect_backend.repository;

import com.farmconnect.farmconnect_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findAllByOrderByDateDesc();

    List<Order> findByStatus(String status);

    List<Order> findByBuyerId(Long buyerId);
    
    List<Order> findByFarmerId(Long farmerId);
}

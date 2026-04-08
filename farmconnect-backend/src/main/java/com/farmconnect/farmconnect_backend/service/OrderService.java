package com.farmconnect.farmconnect_backend.service;

import com.farmconnect.farmconnect_backend.model.Order;
import com.farmconnect.farmconnect_backend.model.OrderItem;
import com.farmconnect.farmconnect_backend.model.User;
import com.farmconnect.farmconnect_backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // ✅ Create Order (SAFE)
    public Order createOrder(User buyer, List<OrderItem> items) {

        // Calculate total securely
        BigDecimal total = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .buyer(buyer)
                .items(items)
                .total(total)
                .status("PENDING")
                .date(LocalDate.now())
                .build();

        // Set order reference
        items.forEach(item -> item.setOrder(order));

        return orderRepository.save(order);
    }

    // Get order by ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // Buyer orders
    public List<Order> getBuyerOrders(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }

    // Status filter
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    // All orders
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByDateDesc();
    }

    // Update status
    public Order updateOrderStatus(Long orderId, String newStatus) {
        Order order = getOrderById(orderId);
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    // Update Order safely
    public Order updateOrder(Long orderId, Order updatedOrder) {
        Order existingOrder = getOrderById(orderId);

        existingOrder.setStatus(updatedOrder.getStatus());

        // Clear old items
        existingOrder.getItems().clear();

        // Add new items safely
        updatedOrder.getItems().forEach(item -> {
            item.setOrder(existingOrder);
            existingOrder.getItems().add(item);
        });

        // Recalculate total
        BigDecimal total = existingOrder.getItems().stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        existingOrder.setTotal(total);

        return orderRepository.save(existingOrder);
    }

    // Delete
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    // Cancel
    public Order cancelOrder(Long orderId) {
        return updateOrderStatus(orderId, "CANCELLED");
    }
}
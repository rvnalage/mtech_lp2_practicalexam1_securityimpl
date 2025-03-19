package com.example.ecommerce.controller;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 📦 Place a new order
    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Order> placeOrder(@RequestBody Order order, Principal principal) {
        Order newOrder = orderService.placeOrder(order, principal.getName());
        return ResponseEntity.ok(newOrder);
    }

    // 📦 Get all orders for the authenticated user
    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<Order>> getUserOrders(Principal principal) {
        List<Order> orders = orderService.getOrdersByUser(principal.getName());
        return ResponseEntity.ok(orders);
    }

    // 📦 Get order details by ID
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId, Principal principal) {
        Order order = orderService.getOrderById(orderId, principal.getName());
        return ResponseEntity.ok(order);
    }

    // 📦 Cancel an order by ID
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> cancelOrder(@PathVariable String orderId, Principal principal) {
        orderService.cancelOrder(orderId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}


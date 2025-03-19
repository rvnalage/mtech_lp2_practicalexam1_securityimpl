package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // 📦 Place a new order
    @Transactional
    public Order placeOrder(Order order, String username) {
        // Get user details
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check product availability and calculate total
        double totalPrice = order.getProducts().stream()
                .map(orderItem -> {
                    Product product = productRepository.findByName(orderItem.getProductName());
                           // .orElseThrow(() -> new RuntimeException("Product not found: " + orderItem.getProductId()));
                    orderItem.setProductName(product.getName());
                    orderItem.setPrice(product.getPrice());
                    return orderItem.getPrice() * orderItem.getQuantity();
                })
                .reduce(0.0, Double::sum);

        // Set order details
        order.setUser(user);
        order.setTotalPrice(totalPrice);
        order.setStatus("PLACED");

        return orderRepository.save(order);
    }

    // 📦 Get all orders for a user
    public List<Order> getOrdersByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user);
    }

    // 📦 Get order details by ID
    public Order getOrderById(String orderId, String username) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getUser().getUsername().equals(username)) {
                return order;
            } else {
                throw new RuntimeException("Unauthorized access");
            }
        }
        throw new RuntimeException("Order not found");
    }

    // 📦 Cancel an order by ID
    @Transactional
    public void cancelOrder(String orderId, String username) {
        Order order = getOrderById(orderId, username);
        if ("PLACED".equals(order.getStatus())) {
            order.setStatus("CANCELLED");
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Order cannot be cancelled");
        }
    }
}


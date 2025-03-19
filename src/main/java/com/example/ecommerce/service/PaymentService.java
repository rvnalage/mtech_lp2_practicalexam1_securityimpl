package com.example.ecommerce.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    public String createOrder(double amount) throws Exception {
        RazorpayClient razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_receipt");
        orderRequest.put("payment_capture", true);

        Order order = razorpayClient.orders.create(orderRequest);
        return order.toString();
    }
}


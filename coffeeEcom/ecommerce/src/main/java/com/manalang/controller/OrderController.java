package com.manalang.controller;

import com.manalang.model.Order;
import com.manalang.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService; 

    @PostMapping("/api/order")
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        log.info("Received new order from frontend: " + order.toString());
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<?> response;

        try {
            Order newOrder = orderService.create(order);
            log.info("Successfully created order with ID: {}", newOrder.getId());
            response = ResponseEntity.ok(newOrder);

        } catch (Exception ex) {
            log.error("Failed to create order: {}", ex.getMessage(), ex);
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating order: " + ex.getMessage());
        }
        return response;
    }
}

package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping(value = "/place-order")
    @ResponseBody
    @CircuitBreaker(name = "inventory", fallbackMethod = "inventoryFallbackMethod")
//    @TimeLimiter(name = "inventory")
//    @Retry(name = "inventory")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        orderService.placeOrder(orderRequest);
        return ResponseEntity.ok("{'m essage': 'The Order Has been Placed!!!'}");
    }

    public ResponseEntity<String> inventoryFallbackMethod(OrderRequest orderRequest, RuntimeException e) {
        return (ResponseEntity<String>) ResponseEntity
                .internalServerError();
    }
}

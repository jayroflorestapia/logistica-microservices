package com.logistica.ordermanagement.controller;

import com.logistica.ordermanagement.dto.CreateOrderRequest;
import com.logistica.ordermanagement.dto.OrderManagementResponse;
import com.logistica.ordermanagement.service.OrderManagementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-management/orders")
public class OrderManagementController {

    private final OrderManagementService orderManagementService;

    public OrderManagementController(
            OrderManagementService orderManagementService
    ) {
        this.orderManagementService = orderManagementService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderManagementResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        return orderManagementService.createOrder(request);
    }

    @GetMapping("/{id}")
    public OrderManagementResponse findOrder(@PathVariable Long id) {
        return orderManagementService.findOrder(id);
    }
}

package com.logistica.order.controller;

import com.logistica.order.domain.Order;
import com.logistica.order.dto.OrderRequest;
import com.logistica.order.dto.OrderResponse;
import com.logistica.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderResponse> findAll(){
        return orderService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }
    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable Long id) {
        return toResponse(orderService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody OrderRequest request) {
        return toResponse(orderService.create(request));
    }

    @PutMapping("/{id}")
    public OrderResponse update(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequest request
    ) {
        return toResponse(orderService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        orderService.delete(id);
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getDescription(),
                order.getQuantity(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }

}

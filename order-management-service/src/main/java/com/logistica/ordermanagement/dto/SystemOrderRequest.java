package com.logistica.ordermanagement.dto;

public record SystemOrderRequest(
        Long customerId,
        String description,
        Integer quantity
) {
}

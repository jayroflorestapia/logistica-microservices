package com.logistica.ordermanagement.dto;

import java.time.LocalDateTime;

public record SystemOrderResponse(
        Long id,
        Long customerId,
        String description,
        Integer quantity,
        String status,
        LocalDateTime createdAt
) {
}

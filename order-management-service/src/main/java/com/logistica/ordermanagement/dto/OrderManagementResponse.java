package com.logistica.ordermanagement.dto;

import java.time.LocalDateTime;

public record OrderManagementResponse(
        Long orderId,
        Long customerId,
        String customerName,
        String customerEmail,
        String description,
        Integer quantity,
        String status,
        LocalDateTime createdAt
) {
}

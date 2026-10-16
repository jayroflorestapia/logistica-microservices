package com.logistica.order.dto;

import java.time.LocalDateTime;

public record  OrderResponse (
    Long id,
    Long customerId,
    String description,
    Integer quantity,
    String status,
    LocalDateTime createdAt
){
}

package com.logistica.ordermanagement.dto;

public record CustomerResponse(
        Long id,
        String name,
        String email
) {
}

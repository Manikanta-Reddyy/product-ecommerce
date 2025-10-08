package com.example.demoe.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    @NotNull(message = "User Id is required")
    private Long userId;

    private LocalDateTime createdAt;

    private String status;

    private BigDecimal totalAmount;

    @Valid
    @NotNull(message = "Order items are required")
    private List<OrderItemDto> items;
}

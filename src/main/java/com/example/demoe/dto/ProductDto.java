package com.example.demoe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long id;

    @NotBlank(message = "product name is required")
    private String name;

    @NotBlank(message = "sku is required")
    private String sku;

    @NotNull(message = "price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private String description;
}

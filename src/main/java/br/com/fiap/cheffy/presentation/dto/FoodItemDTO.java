package br.com.fiap.cheffy.presentation.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record FoodItemDTO(

        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotBlank
        BigDecimal price,
        @NotBlank
        String photoKey,
        boolean deliveryAvailable,
        boolean available
) {
}

package br.com.fiap.cheffy.presentation.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record FoodItemDTO(

        @NotBlank
        String name,
        String description,
        @NotBlank
        BigDecimal price,
        String photoKey,
        boolean deliveryAvailable,
        boolean available,
        boolean active
) {
}

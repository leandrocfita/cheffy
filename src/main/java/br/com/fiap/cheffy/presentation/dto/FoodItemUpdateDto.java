package br.com.fiap.cheffy.presentation.dto;

import br.com.fiap.cheffy.presentation.interfaces.FoodItemRequest;

import java.math.BigDecimal;

public record FoodItemUpdateDto(
        String name,
        String description,
        BigDecimal price,
        String photoKey,
        boolean deliveryAvailable,
        boolean available,
        boolean active
) implements FoodItemRequest {
}

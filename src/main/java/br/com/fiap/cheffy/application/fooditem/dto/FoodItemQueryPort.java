package br.com.fiap.cheffy.application.fooditem.dto;

import br.com.fiap.cheffy.domain.valueobject.Money;

import java.math.BigDecimal;
import java.util.UUID;

public record FoodItemQueryPort(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        String photoKey,
        UUID restaurantId,
        Boolean deliveryAvailable,
        Boolean available,
        Boolean active
) {
}

package br.com.fiap.cheffy.application.restaurant.dto;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;

import java.time.OffsetTime;
import java.util.Set;
import java.util.UUID;

public record RestaurantQueryPort(
        UUID id,
        String name,
        String culinary,
        OffsetTime openingTime,
        OffsetTime closingTime,
        AddressCommandPort address,
        Set<FoodItemQueryPort> menu
) {
}

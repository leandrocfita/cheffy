package br.com.fiap.cheffy.application.restaurant.dto;

import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;

import java.time.OffsetTime;

public record RestaurantCommandPort(
        String name,
        String culinary,
        OffsetTime openingTime,
        OffsetTime closingTime,
        AddressCommandPort address
) {
}

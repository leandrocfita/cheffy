package br.com.fiap.cheffy.domain.restaurant.port;

import br.com.fiap.cheffy.application.restaurant.dto.RestaurantCommandPort;

import java.util.UUID;

public interface RegisterRestaurantInput {

    String execute(RestaurantCommandPort restaurant, UUID userId);
}

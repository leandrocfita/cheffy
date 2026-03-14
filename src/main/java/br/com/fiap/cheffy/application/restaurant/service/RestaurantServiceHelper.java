package br.com.fiap.cheffy.application.restaurant.service;

import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.RESTAURANT_NOT_FOUND_EXCEPTION;

public class RestaurantServiceHelper {
    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceHelper(
            RestaurantRepository restaurantRepository
    ) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant getRestaurantOrFail(UUID id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(RESTAURANT_NOT_FOUND_EXCEPTION, id));
    }

    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }
}

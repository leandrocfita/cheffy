package br.com.fiap.cheffy.application.restaurant.usecase;

import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import br.com.fiap.cheffy.domain.restaurant.port.input.RestaurantActivationProcessInput;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.RESTAURANT_NOT_FOUND_EXCEPTION;
import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.RESTAURANT_USER_DOES_NOT_HAVE_OWNERSHIP_OR_IS_INACTIVE;

public class ReactivateRestaurantUseCase implements RestaurantActivationProcessInput {
    private final RestaurantRepository restaurantRepository;

    public ReactivateRestaurantUseCase(
            RestaurantRepository restaurantRepository
    ) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void execute(UUID id, UUID userId) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(RESTAURANT_NOT_FOUND_EXCEPTION, id));

        if (!restaurant.isOwnedByUser(userId)) {
            throw new RestaurantOperationNotAllowedException(RESTAURANT_USER_DOES_NOT_HAVE_OWNERSHIP_OR_IS_INACTIVE);
        }

        restaurant.reactivate();
        restaurantRepository.save(restaurant);
    }
}

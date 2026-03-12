package br.com.fiap.cheffy.application.restaurant.usecase;

import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReactivateRestaurantUseCaseTest {

    @Mock
    private RestaurantServiceHelper restaurantServiceHelper;

    private ReactivateRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ReactivateRestaurantUseCase(restaurantServiceHelper);
    }

    @Test
    void executeReactivatesRestaurantWhenOwnerIsValid() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantServiceHelper.getRestaurantOrFail(id)).thenReturn(restaurant);
        when(restaurant.isOwnedByUser(userId)).thenReturn(true);

        useCase.execute(id, userId);

        verify(restaurant).reactivate();
        verify(restaurantServiceHelper).saveRestaurant(restaurant);
    }

    @Test
    void executeThrowsWhenRestaurantNotFound() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(restaurantServiceHelper.getRestaurantOrFail(id))
                .thenThrow(new RestaurantNotFoundException(
                        br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.RESTAURANT_NOT_FOUND_EXCEPTION, id));

        assertThrows(RestaurantNotFoundException.class, () -> useCase.execute(id, userId));
        verify(restaurantServiceHelper, never()).saveRestaurant(any());
    }

    @Test
    void executeThrowsWhenUserDoesNotOwnRestaurant() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantServiceHelper.getRestaurantOrFail(id)).thenReturn(restaurant);
        when(restaurant.isOwnedByUser(userId)).thenReturn(false);

        assertThrows(RestaurantOperationNotAllowedException.class, () -> useCase.execute(id, userId));
        verify(restaurant, never()).reactivate();
        verify(restaurantServiceHelper, never()).saveRestaurant(any());
    }
}

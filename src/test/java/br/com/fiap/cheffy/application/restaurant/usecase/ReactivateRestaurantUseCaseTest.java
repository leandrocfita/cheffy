package br.com.fiap.cheffy.application.restaurant.usecase;

import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReactivateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private ReactivateRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ReactivateRestaurantUseCase(restaurantRepository);
    }

    @Test
    void executeReactivatesRestaurantWhenOwnerIsValid() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurant.isOwnedByUser(userId)).thenReturn(true);

        useCase.execute(id, userId);

        verify(restaurant).reactivate();
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void executeThrowsWhenRestaurantNotFound() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(restaurantRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> useCase.execute(id, userId));
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void executeThrowsWhenUserDoesNotOwnRestaurant() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));
        when(restaurant.isOwnedByUser(userId)).thenReturn(false);

        assertThrows(RestaurantOperationNotAllowedException.class, () -> useCase.execute(id, userId));
        verify(restaurant, never()).reactivate();
        verify(restaurantRepository, never()).save(any());
    }
}

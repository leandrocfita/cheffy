package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.domain.fooditem.port.input.CreateFoodItemInput;
import br.com.fiap.cheffy.domain.fooditem.port.input.FindFoodItemByIdInput;
import br.com.fiap.cheffy.presentation.mapper.FoodItemWebMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FoodItemControllerTest {
    @Mock
    private CreateFoodItemInput createFoodItemInput;
    @Mock
    private FindFoodItemByIdInput findFoodItemByIdInput;
    @Mock
    private FoodItemWebMapper foodItemWebMapper;
    @InjectMocks
    private FoodItemController controller;

    @Test
    void getFoodItemByIdReturnsOkWithFoodItem() {
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();
        FoodItemQueryPort queryPort = new FoodItemQueryPort(
                foodItemId, "Pizza", "Margherita", BigDecimal.TEN,
                "photo-key", restaurantId, true, true, true
        );
        when(findFoodItemByIdInput.execute(restaurantId, foodItemId)).thenReturn(queryPort);
        ResponseEntity<FoodItemQueryPort> response = controller.getFoodItemById(restaurantId, foodItemId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(queryPort);
        assertThat(response.getBody().id()).isEqualTo(foodItemId);
        assertThat(response.getBody().restaurantId()).isEqualTo(restaurantId);
        verify(findFoodItemByIdInput).execute(restaurantId, foodItemId);
    }
}
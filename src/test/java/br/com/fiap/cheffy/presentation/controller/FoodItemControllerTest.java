package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;
import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.port.input.CreateFoodItemInput;
import br.com.fiap.cheffy.domain.fooditem.port.input.UpdateFoodItemInput;
import br.com.fiap.cheffy.domain.valueobject.Money;
import br.com.fiap.cheffy.presentation.dto.FoodItemDTO;
import br.com.fiap.cheffy.presentation.dto.FoodItemUpdateDto;
import br.com.fiap.cheffy.presentation.mapper.FoodItemWebMapper;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FoodItemControllerTest {

    @Mock
    private CreateFoodItemInput createFoodItemInput;

    @Mock
    private UpdateFoodItemInput updateFoodItemInput;

    @Mock
    private FoodItemWebMapper foodItemWebMapper;

    @InjectMocks
    private FoodItemController foodItemController;

    @Test
    @DisplayName("Should return 201 Created when a food item is successfully created")
    void postFoodItemReturnsCreated() {
        UUID restaurantId = UUID.randomUUID();
        FoodItemDTO dto = new FoodItemDTO("Name", "Desc", BigDecimal.TEN, "photo", true, true, true);
        FoodItemCommandPort commandPort = new FoodItemCommandPort("Name", "Desc", BigDecimal.TEN, "photo", restaurantId, true, true, true);
        FoodItem createdFoodItem = FoodItem.reconstitute(UUID.randomUUID(), "Name", "Desc", BigDecimal.TEN, "photo", true, true, true);
        FoodItemQueryPort queryPort = new FoodItemQueryPort(createdFoodItem.getId(), "Name", "Desc", new Money(BigDecimal.TEN), "photo", restaurantId, true, true, true);

        when(foodItemWebMapper.foodItemDtoToFoodItemCommandPort(dto, restaurantId)).thenReturn(commandPort);
        when(createFoodItemInput.execute(commandPort)).thenReturn(createdFoodItem);
        when(foodItemWebMapper.foodItemToFoodItemQueryPort(createdFoodItem)).thenReturn(queryPort);

        ResponseEntity<Object> response = foodItemController.postFoodItem(dto, restaurantId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(queryPort);
    }

    @Test
    @DisplayName("Should return 204 No Content when a food item is successfully updated")
    void updateFoodItemReturnsNoContent() {
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        FoodItemUpdateDto dto = new FoodItemUpdateDto("Name", "Desc", BigDecimal.TEN, "photo", true, true, true);
        FoodItemCommandPort commandPort = new FoodItemCommandPort("Name", "Desc", BigDecimal.TEN, "photo", restaurantId, true, true, true);

        when(foodItemWebMapper.foodItemDtoToFoodItemCommandPort(dto, restaurantId)).thenReturn(commandPort);

        ResponseEntity<Object> response = foodItemController.updateFoodItem(dto, restaurantId, userId, foodItemId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(updateFoodItemInput).update(eq(foodItemId), eq(restaurantId), eq(userId), eq(commandPort));
    }
}

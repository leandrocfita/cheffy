package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;
import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.port.input.CreateFoodItemInput;
import br.com.fiap.cheffy.domain.fooditem.port.input.UpdateFoodItemInput;
import br.com.fiap.cheffy.domain.fooditem.port.input.ListFoodItemsByRestaurantInput;
import br.com.fiap.cheffy.presentation.dto.FoodItemDTO;
import br.com.fiap.cheffy.domain.fooditem.port.input.FindFoodItemByIdInput;
import br.com.fiap.cheffy.presentation.dto.FoodItemUpdateDto;
import br.com.fiap.cheffy.presentation.mapper.FoodItemWebMapper;
import br.com.fiap.cheffy.utils.FoodItemTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FoodItemControllerTest {

    @Mock
    private CreateFoodItemInput createFoodItemInput;

    @Mock
    private ListFoodItemsByRestaurantInput listFoodItemsByRestaurantInput;

    @Mock
    private FindFoodItemByIdInput findFoodItemByIdInput;

    @Mock
    private UpdateFoodItemInput updateFoodItemInput;

    @Mock
    private FoodItemWebMapper foodItemWebMapper;

    @InjectMocks
    private FoodItemController foodItemController;

    @Test
    void getFoodItemByIdReturnsOkWithFoodItem() {
        UUID restaurantId = UUID.randomUUID();
        UUID foodItemId = UUID.randomUUID();
        FoodItemQueryPort queryPort = FoodItemTestUtils.createTestFoodItemQueryPort(foodItemId, restaurantId);
        when(findFoodItemByIdInput.execute(restaurantId, foodItemId)).thenReturn(queryPort);
        ResponseEntity<FoodItemQueryPort> response = foodItemController.getFoodItemById(restaurantId, foodItemId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(queryPort);
        assertThat(response.getBody().id()).isEqualTo(foodItemId);
        assertThat(response.getBody().restaurantId()).isEqualTo(restaurantId);
        verify(findFoodItemByIdInput).execute(restaurantId, foodItemId);
    }

    @Test
    @DisplayName("GET should return 200 with paginated food items")
    void listFoodItemsByRestaurantReturnsOk() {
        UUID restaurantId = UUID.randomUUID();
        FoodItemQueryPort queryPort = FoodItemTestUtils.createTestFoodItemQueryPort(UUID.randomUUID(), restaurantId);
        PageResult<FoodItemQueryPort> page = PageResult.of(List.of(queryPort), 0, 10, 1L);

        when(listFoodItemsByRestaurantInput.execute(eq(restaurantId), any(PageRequest.class), eq(false))).thenReturn(page);

        ResponseEntity<PageResult<FoodItemQueryPort>> response =
                foodItemController.listFoodItemsByRestaurant(restaurantId, 0, 10, "name", Sort.Direction.ASC, false);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content()).hasSize(1);
        verify(listFoodItemsByRestaurantInput).execute(eq(restaurantId), any(PageRequest.class), eq(false));
    }

    @Test
    @DisplayName("GET should use DESC sort direction when specified")
    void listFoodItemsByRestaurantWithDescDirection() {
        UUID restaurantId = UUID.randomUUID();
        PageResult<FoodItemQueryPort> page = PageResult.of(List.of(), 0, 10, 0L);

        when(listFoodItemsByRestaurantInput.execute(eq(restaurantId), any(PageRequest.class), eq(false))).thenReturn(page);

        ResponseEntity<PageResult<FoodItemQueryPort>> response =
                foodItemController.listFoodItemsByRestaurant(restaurantId, 0, 10, "name", Sort.Direction.DESC, false);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("POST should return 201 with created food item")
    void postFoodItemReturnsCreated() {
        UUID restaurantId = UUID.randomUUID();
        FoodItemDTO dto = new FoodItemDTO("Name", "Desc", BigDecimal.TEN, "photo", true, true, true);
        FoodItemCommandPort commandPort = new FoodItemCommandPort("Name", "Desc", BigDecimal.TEN, "photo", restaurantId, true, true, true);
        FoodItem createdFoodItem = FoodItem.reconstitute(UUID.randomUUID(), "Name", "Desc", BigDecimal.TEN, "photo", true, true, true);
        FoodItemQueryPort queryPort = new FoodItemQueryPort(createdFoodItem.getId(), "Name", "Desc", BigDecimal.TEN, "photo", restaurantId, true, true, true);
        FoodItem createdItem = FoodItemTestUtils.createTestFoodItemDomainEntity();

        when(foodItemWebMapper.foodItemDtoToFoodItemCommandPort(dto, restaurantId)).thenReturn(commandPort);
        when(createFoodItemInput.execute(commandPort)).thenReturn(createdFoodItem);
        when(createFoodItemInput.execute(commandPort)).thenReturn(createdItem);
        when(foodItemWebMapper.foodItemToFoodItemQueryPort(createdItem)).thenReturn(queryPort);

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

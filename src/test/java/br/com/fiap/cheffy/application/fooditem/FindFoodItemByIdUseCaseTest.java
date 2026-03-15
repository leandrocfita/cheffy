package br.com.fiap.cheffy.application.fooditem;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.application.fooditem.usecase.FindFoodItemByIdUseCase;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemNotFoundException;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.presentation.mapper.FoodItemWebMapper;
import br.com.fiap.cheffy.utils.FoodItemTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindFoodItemByIdUseCaseTest {
    @Mock
    private FoodItemRepository foodItemRepository;
    @Mock
    private FoodItemWebMapper foodItemWebMapper;

    private FindFoodItemByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindFoodItemByIdUseCase(foodItemRepository, foodItemWebMapper);
    }

    @Test
    void shouldReturnFoodItemWhenFoundById() {
        FoodItem foodItem = FoodItemTestUtils.createTestFoodItemDomainEntity();
        UUID foodItemId = foodItem.getId();
        FoodItemQueryPort queryPort = mock(FoodItemQueryPort.class);
        when(foodItemRepository.findById(foodItemId)).thenReturn(Optional.of(foodItem));
        when(foodItemWebMapper.foodItemToFoodItemQueryPort(foodItem)).thenReturn(queryPort);


        FoodItemQueryPort executed = useCase.execute(foodItemId);


        assertNotNull(executed);
        assertEquals(queryPort, executed);
        verify(foodItemRepository, times(1)).findById(foodItemId);
        verify(foodItemWebMapper, times(1)).foodItemToFoodItemQueryPort(foodItem);
    }

    @Test
    void shouldThrowFoodItemNotFoundExceptionWhenNotFound() {
        UUID foodItemId = UUID.randomUUID();
        when(foodItemRepository.findById(foodItemId)).thenReturn(Optional.empty());

        assertThrows(FoodItemNotFoundException.class, () -> useCase.execute(foodItemId));

        verify(foodItemRepository, times(1)).findById(foodItemId);
        verify(foodItemWebMapper, never()).foodItemToFoodItemQueryPort(any());
    }

    @Test
    void shouldCorrectlyMapFoodItemWithRestaurant() {
        FoodItem foodItem = FoodItemTestUtils.createTestFoodItemDomainEntity();
        UUID foodItemId = foodItem.getId();
        FoodItemQueryPort queryPort = mock(FoodItemQueryPort.class);
        when(foodItemRepository.findById(foodItemId)).thenReturn(Optional.of(foodItem));
        when(foodItemWebMapper.foodItemToFoodItemQueryPort(foodItem)).thenReturn(queryPort);

        useCase.execute(foodItemId);

        verify(foodItemWebMapper).foodItemToFoodItemQueryPort(argThat(item ->
                item.getName().equals("Test Food") &&
                        item.getDescription().equals("Delicious test food") &&
                        item.getRestaurant() != null
        ));
    }
}
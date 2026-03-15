package br.com.fiap.cheffy.application.fooditem.usecase;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemNotFoundException;
import br.com.fiap.cheffy.domain.fooditem.port.input.FindFoodItemByIdInput;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.presentation.mapper.FoodItemWebMapper;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.FOOD_ITEM_NOT_FOUND_EXCEPTION;

public class FindFoodItemByIdUseCase implements FindFoodItemByIdInput {
    private final FoodItemRepository foodItemRepository;
    private final FoodItemWebMapper foodItemWebMapper;

    public FindFoodItemByIdUseCase(FoodItemRepository foodItemRepository, FoodItemWebMapper foodItemWebMapper) {
        this.foodItemRepository = foodItemRepository;
        this.foodItemWebMapper = foodItemWebMapper;
    }

    @Override
    public FoodItemQueryPort execute(UUID foodItemId) {
        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new FoodItemNotFoundException(FOOD_ITEM_NOT_FOUND_EXCEPTION, foodItemId));
        return foodItemWebMapper.foodItemToFoodItemQueryPort(foodItem);
    }
}
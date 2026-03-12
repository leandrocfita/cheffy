package br.com.fiap.cheffy.domain.fooditem.port.input;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;

import java.util.UUID;

public interface CreateFoodItemInput {

    FoodItem execute(FoodItemCommandPort foodItemCommandPort, UUID restaurantId);
}

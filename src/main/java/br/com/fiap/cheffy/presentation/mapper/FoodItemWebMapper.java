package br.com.fiap.cheffy.presentation.mapper;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.presentation.dto.FoodItemDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FoodItemWebMapper {

    public FoodItemQueryPort FoodItemToFoodItemQueryPort(FoodItemDTO foodItemDTO, String restaurantId){

        return new FoodItemQueryPort(
                null,
                foodItemDTO.name(),
                foodItemDTO.description(),
                foodItemDTO.price(),
                foodItemDTO.photoKey(),
                UUID.fromString(restaurantId),
                foodItemDTO.deliveryAvailable(),
                foodItemDTO.available(),
                foodItemDTO.active()
        );
    }
}

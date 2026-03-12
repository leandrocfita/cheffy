package br.com.fiap.cheffy.domain.fooditem.port.output;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodItemRepository {

    FoodItem save(FoodItem foodItem);
    Optional<FoodItem> findById(UUID foodItemId);
    List<FoodItem> findAllByRestaurantId(UUID restaurantId);
    boolean existsInRestaurantById(UUID restaurantId, UUID foodItemId);
    boolean existsByNameIgnoreCaseAndRestaurantId(String foodName, UUID restaurantId);
}

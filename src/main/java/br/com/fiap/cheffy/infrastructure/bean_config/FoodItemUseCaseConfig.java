package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.fooditem.usecase.CreateFoodItemUseCase;
import br.com.fiap.cheffy.application.fooditem.usecase.UpdateFoodItemUseCase;
import br.com.fiap.cheffy.application.fooditem.usecase.ListFoodItemsByRestaurantUseCase;
import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
import br.com.fiap.cheffy.application.fooditem.usecase.FindFoodItemByIdUseCase;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import br.com.fiap.cheffy.presentation.mapper.FoodItemWebMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FoodItemUseCaseConfig {

    @Bean
    CreateFoodItemUseCase createFoodItemUseCase(FoodItemRepository foodItemRepository, RestaurantRepository restaurantRepository) {
        return new CreateFoodItemUseCase(foodItemRepository, restaurantRepository);
    }

    @Bean
    UpdateFoodItemUseCase updateFoodItemUseCase(FoodItemRepository foodItemRepository, RestaurantRepository restaurantRepository){
        return new UpdateFoodItemUseCase(foodItemRepository, restaurantRepository);
    }

    @Bean
    ListFoodItemsByRestaurantUseCase listFoodItemsByRestaurantUseCase(FoodItemRepository foodItemRepository, RestaurantServiceHelper restaurantServiceHelper) {
        return new ListFoodItemsByRestaurantUseCase(foodItemRepository, restaurantServiceHelper);
    }

    @Bean
    FindFoodItemByIdUseCase findFoodItemByIdUseCase(FoodItemRepository foodItemRepository) {
        return new FindFoodItemByIdUseCase(foodItemRepository);
    }
}

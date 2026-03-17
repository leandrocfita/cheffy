package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.fooditem.usecase.CreateFoodItemUseCase;
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
    FindFoodItemByIdUseCase findFoodItemByIdUseCase(FoodItemRepository foodItemRepository) {
        return new FindFoodItemByIdUseCase(foodItemRepository);
    }
}

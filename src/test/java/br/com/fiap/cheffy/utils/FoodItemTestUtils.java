package br.com.fiap.cheffy.utils;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.entity.FoodItemJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class FoodItemTestUtils {

    public static FoodItem createTestFoodItemDomainEntity() {
        UUID id = UUID.randomUUID();
        FoodItem foodItem = FoodItem.reconstitute(
                id,
                "Test Food",
                "Delicious test food",
                new BigDecimal("19.99"),
                "test-photo-key",
                true,
                true,
                true
        );
        foodItem.setRestaurant(RestaurantTestUtils.createTestRestaurantDomainEntity());
        return foodItem;
    }

    public static FoodItemJpaEntity createTestFoodItemJpaEntity() {
        FoodItemJpaEntity entity = new FoodItemJpaEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("Test Food");
        entity.setDescription("Delicious test food");
        entity.setPrice(new BigDecimal("19.99"));
        entity.setPhotoKey("test-photo-key");
        entity.setDeliveryAvailable(true);
        entity.setAvailable(true);
        entity.setActive(true);
        entity.setDateCreated(OffsetDateTime.now());
        entity.setLastUpdated(OffsetDateTime.now());

        RestaurantJpaEntity restaurant = RestaurantTestUtils.createTestRestaurantJpaEntity();
        entity.setRestaurant(restaurant);
        
        return entity;
    }
}

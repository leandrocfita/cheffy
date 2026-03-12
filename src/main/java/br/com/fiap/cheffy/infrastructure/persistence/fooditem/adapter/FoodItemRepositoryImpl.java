package br.com.fiap.cheffy.infrastructure.persistence.fooditem.adapter;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.entity.FoodItemJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.mapper.FoodItemPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.repository.FoodItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FoodItemRepositoryImpl implements FoodItemRepository {

    private final FoodItemPersistenceMapper foodItemPersistenceMapper;
    private final FoodItemJpaRepository foodItemJpaRepository;


    @Override
    public FoodItem save(FoodItem foodItem) {

        FoodItemJpaEntity transformedObject = foodItemPersistenceMapper.toJpa(foodItem);

         var savedEntity = foodItemJpaRepository.save(transformedObject);

        return foodItemPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<FoodItem> findById(UUID foodItemId) {
        return Optional.empty();
    }

    @Override
    public List<FoodItem> findAllByRestaurantId(UUID restaurantId) {
        return List.of();
    }

    @Override
    public boolean existsInRestaurantById(UUID restaurantId, UUID foodItemId) {
        return foodItemJpaRepository.existsInRestaurantById(restaurantId, foodItemId);
    }

    @Override
    public boolean existsInRestaurantByName(String foodName, UUID restaurantId) {
        return foodItemJpaRepository.existsInRestaurantByName(foodName, restaurantId);
    }
}

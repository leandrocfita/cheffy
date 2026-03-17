package br.com.fiap.cheffy.infrastructure.persistence.fooditem.adapter;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.entity.FoodItemJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.mapper.FoodItemPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.repository.FoodItemJpaRepository;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.mapper.RestaurantPersistenceMapper;
import br.com.fiap.cheffy.utils.FoodItemTestUtils;
import br.com.fiap.cheffy.utils.RestaurantTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodItemRepositoryImplTest {
    @Mock
    private FoodItemPersistenceMapper foodItemPersistenceMapper;
    @Mock
    private FoodItemJpaRepository foodItemJpaRepository;
    @Mock
    private RestaurantPersistenceMapper restaurantPersistenceMapper;
    @InjectMocks
    private FoodItemRepositoryImpl repository;

    @Test
    void findByIdAndRestaurantIdReturnsMappedDomainWhenFound() {
        UUID foodItemId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        FoodItemJpaEntity jpaEntity = FoodItemTestUtils.createTestFoodItemJpaEntity();
        FoodItem domainFoodItem = FoodItemTestUtils.createTestFoodItemDomainEntity();
        Restaurant domainRestaurant = RestaurantTestUtils.createTestRestaurantDomainEntity();
        RestaurantJpaEntity restaurantJpaEntity = jpaEntity.getRestaurant();
        when(foodItemJpaRepository.findByIdAndRestaurantId(foodItemId, restaurantId)).thenReturn(Optional.of(jpaEntity));
        when(foodItemPersistenceMapper.toDomain(jpaEntity)).thenReturn(domainFoodItem);
        when(restaurantPersistenceMapper.toDomain(restaurantJpaEntity)).thenReturn(domainRestaurant);
        Optional<FoodItem> result = repository.findByIdAndRestaurantId(foodItemId, restaurantId);
        assertThat(result).isPresent();
        assertThat(result.get().getRestaurant()).isEqualTo(domainRestaurant);
        verify(foodItemJpaRepository).findByIdAndRestaurantId(foodItemId, restaurantId);
        verify(foodItemPersistenceMapper).toDomain(jpaEntity);
        verify(restaurantPersistenceMapper).toDomain(restaurantJpaEntity);
    }

    @Test
    void findByIdAndRestaurantIdReturnsEmptyWhenNotFound() {
        UUID foodItemId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();
        when(foodItemJpaRepository.findByIdAndRestaurantId(foodItemId, restaurantId)).thenReturn(Optional.empty());
        Optional<FoodItem> result = repository.findByIdAndRestaurantId(foodItemId, restaurantId);
        assertThat(result).isEmpty();
        verify(foodItemJpaRepository).findByIdAndRestaurantId(foodItemId, restaurantId);
        verify(foodItemPersistenceMapper, never()).toDomain(any(FoodItemJpaEntity.class));
    }

    @Test
    void findByIdReturnsMappedDomainWhenFound() {
        UUID foodItemId = UUID.randomUUID();
        FoodItemJpaEntity jpaEntity = FoodItemTestUtils.createTestFoodItemJpaEntity();
        FoodItem domainFoodItem = FoodItemTestUtils.createTestFoodItemDomainEntity();
        Restaurant domainRestaurant = RestaurantTestUtils.createTestRestaurantDomainEntity();
        RestaurantJpaEntity restaurantJpaEntity = jpaEntity.getRestaurant();
        when(foodItemJpaRepository.findById(foodItemId)).thenReturn(Optional.of(jpaEntity));
        when(foodItemPersistenceMapper.toDomain(jpaEntity)).thenReturn(domainFoodItem);
        when(restaurantPersistenceMapper.toDomain(restaurantJpaEntity)).thenReturn(domainRestaurant);
        Optional<FoodItem> result = repository.findById(foodItemId);
        assertThat(result).isPresent();
        assertThat(result.get().getRestaurant()).isEqualTo(domainRestaurant);
        verify(foodItemJpaRepository).findById(foodItemId);
        verify(foodItemPersistenceMapper).toDomain(jpaEntity);
    }

    @Test
    void findByIdReturnsEmptyWhenNotFound() {
        UUID foodItemId = UUID.randomUUID();
        when(foodItemJpaRepository.findById(foodItemId)).thenReturn(Optional.empty());
        Optional<FoodItem> result = repository.findById(foodItemId);
        assertThat(result).isEmpty();
        verify(foodItemJpaRepository).findById(foodItemId);
    }
}
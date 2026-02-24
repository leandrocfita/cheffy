package br.com.fiap.cheffy.infrastructure.persistence.restaurant.adapter;

import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.mapper.RestaurantPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.repository.RestaurantJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantRepositoryImplTest {

    @Mock
    private RestaurantJpaRepository restaurantJpaRepository;

    @Mock
    private RestaurantPersistenceMapper restaurantMapper;

    @InjectMocks
    private RestaurantRepositoryImpl repository;

    @Test
    void saveMapsAndPersistsRestaurant() {
        Restaurant restaurant = org.mockito.Mockito.mock(Restaurant.class);
        RestaurantJpaEntity jpaEntity = new RestaurantJpaEntity();
        RestaurantJpaEntity savedJpaEntity = new RestaurantJpaEntity();
        Restaurant savedDomain = org.mockito.Mockito.mock(Restaurant.class);

        when(restaurantMapper.toJpa(restaurant)).thenReturn(jpaEntity);
        when(restaurantJpaRepository.save(jpaEntity)).thenReturn(savedJpaEntity);
        when(restaurantMapper.toDomain(savedJpaEntity)).thenReturn(savedDomain);

        Restaurant result = repository.save(restaurant);

        assertThat(result).isEqualTo(savedDomain);
        verify(restaurantMapper).toJpa(restaurant);
        verify(restaurantJpaRepository).save(jpaEntity);
        verify(restaurantMapper).toDomain(savedJpaEntity);
    }

    @Test
    void existsByCnpjDelegatesToJpaRepository() {
        when(restaurantJpaRepository.existsByCnpj( "27865757000102")).thenReturn(true);

        boolean result = repository.existsByCnpj("27865757000102");

        assertThat(result).isTrue();
        verify(restaurantJpaRepository).existsByCnpj( "27865757000102");
    }

    @Test
    void existsByNameDelegatesToJpaRepository() {
        when(restaurantJpaRepository.existsByName( "Name")).thenReturn(true);

        boolean result = repository.existsByName("Name");

        assertThat(result).isTrue();
        verify(restaurantJpaRepository).existsByName(( "Name"));
    }
}

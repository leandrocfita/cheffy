package br.com.fiap.cheffy.infrastructure.persistence.restaurant.adapter;

import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.mapper.RestaurantPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.repository.RestaurantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantPersistenceMapper restaurantMapper;


    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantJpaEntity restaurantJpaEntity = restaurantMapper.toJpa(restaurant);

        RestaurantJpaEntity saved = restaurantJpaRepository.save(restaurantJpaEntity);

        return restaurantMapper.toDomain(saved);
    }
}

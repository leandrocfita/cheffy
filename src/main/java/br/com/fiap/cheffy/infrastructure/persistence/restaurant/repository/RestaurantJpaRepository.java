package br.com.fiap.cheffy.infrastructure.persistence.restaurant.repository;

import br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity.RestaurantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantJpaEntity, UUID> {

    boolean existsByCnpj(String cnpj);
    boolean existsByName(String restaurantName);
    boolean existsByUserIdAndActiveTrue(UUID userId);
}

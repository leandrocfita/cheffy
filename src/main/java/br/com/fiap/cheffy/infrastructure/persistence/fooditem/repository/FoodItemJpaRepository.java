package br.com.fiap.cheffy.infrastructure.persistence.fooditem.repository;

import br.com.fiap.cheffy.infrastructure.persistence.fooditem.entity.FoodItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FoodItemJpaRepository extends JpaRepository<FoodItemJpaEntity, UUID> {

    @Query(""" 
        SELECT f FROM FoodItemJpaEntity f
        JOIN FETCH f.restaurant r
        WHERE r.id = :restaurantId
    """)
    List<FoodItemJpaEntity> findAllByRestaurantId(@Param("restaurantId") UUID restaurantId);
    
    
    @Query("""
        SELECT COUNT(f) > 0
        FROM FoodItemJpaEntity f
        WHERE f.name = :foodName
        AND f.restaurant.id = :restaurantId
    """)
    boolean existsInRestaurantByName(@Param("foodName") String foodName, @Param("restaurantId") UUID restaurantId);

    @Query("""
        SELECT COUNT(f) > 0
        FROM FoodItemJpaEntity f
        WHERE f.id = :foodItemId
        AND f.restaurant.id = :restaurantId
    """)
    boolean existsInRestaurantById(@Param("restaurantId") UUID restaurantId, @Param("foodItemId") UUID foodItemId);

}

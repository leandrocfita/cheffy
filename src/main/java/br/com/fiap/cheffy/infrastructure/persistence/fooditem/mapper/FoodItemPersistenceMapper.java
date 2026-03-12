package br.com.fiap.cheffy.infrastructure.persistence.fooditem.mapper;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.entity.FoodItemJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class FoodItemPersistenceMapper {

    public FoodItem toDomain(FoodItemJpaEntity entity) {
        return FoodItem.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getPhotoKey(),
                entity.getDeliveryAvailable(),
                entity.getAvailable(),
                entity.getActive()
        );
    }

    public FoodItemJpaEntity toJpa(FoodItem domain) {

        FoodItemJpaEntity entity = new FoodItemJpaEntity();

        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setPrice(domain.getPrice().value());
        entity.setPhotoKey(domain.getPhotoKey());
        entity.setDeliveryAvailable(domain.isDeliveryAvailable());
        entity.setAvailable(domain.isAvailable());
        entity.setActive(domain.isActive());

        return entity;
    }
}

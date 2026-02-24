package br.com.fiap.cheffy.infrastructure.persistence.fooditen.mapper;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.infrastructure.persistence.fooditen.entity.FoodItemJpaEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FoodItemPersistenceMapperTest {

    private final FoodItemPersistenceMapper mapper = new FoodItemPersistenceMapper();

    @Test
    void toDomainMapsJpaEntityToDomain() {
        UUID id = UUID.randomUUID();
        FoodItemJpaEntity entity = new FoodItemJpaEntity();
        entity.setId(id);
        entity.setName("Prato");
        entity.setDescription("Descrição");
        entity.setPrice(BigDecimal.TEN);
        entity.setPhotoKey("key");
        entity.setDeliveryAvailable(true);
        entity.setAvailable(true);
        entity.setActive(true);

        FoodItem result = mapper.toDomain(entity);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Prato");
        assertThat(result.getPrice().value()).isEqualByComparingTo("10.00");
        assertThat(result.isAvailable()).isTrue();
    }

    @Test
    void toJpaMapsDomainToJpaEntity() {
        UUID id = UUID.randomUUID();
        FoodItem domain = FoodItem.reconstitute(id, "Prato", "Descrição", BigDecimal.TEN, "key", true, true, true);

        FoodItemJpaEntity result = mapper.toJpa(domain);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Prato");
        assertThat(result.getPrice()).isEqualByComparingTo("10.00");
        assertThat(result.getDeliveryAvailable()).isTrue();
        assertThat(result.getAvailable()).isTrue();
        assertThat(result.getActive()).isTrue();
    }
}

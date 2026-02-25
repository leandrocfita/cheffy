package br.com.fiap.cheffy.domain.fooditem.entity;

import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FoodItemTest {

    @Test
    void createSetsRestaurantAndAvailabilityState() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.createWithWorkingHours(
                "Rest",
                "27865757000102",
                "Brasileira",
                zoneId,
                LocalTime.parse("09:00"),
                LocalTime.parse("18:00"),
                null
        );

        FoodItem item = FoodItem.create("Prato", "Desc", BigDecimal.TEN, "key", true, true, restaurant);

        assertThat(item.getRestaurant()).isEqualTo(restaurant);
        assertThat(item.isAvailable()).isTrue();
        assertThat(item.getPrice().value()).isEqualByComparingTo("10.00");
    }

    @Test
    void makeUnavailableAndDisableChangeAvailability() {
        FoodItem item = FoodItem.reconstitute(UUID.randomUUID(), "Prato", "Desc", BigDecimal.TEN, "key", true, true, true);

        item.makeUnavailable();
        assertThat(item.isAvailable()).isFalse();

        item.disable();
        assertThat(item.isActive()).isFalse();
        assertThat(item.isAvailable()).isFalse();
    }

    @Test
    void equalsAndHashCodeUseId() {
        UUID id = UUID.randomUUID();
        FoodItem first = FoodItem.reconstitute(id, "Prato", "Desc", BigDecimal.TEN, "key", true, true, true);
        FoodItem second = FoodItem.reconstitute(id, "Outro", "Outro", BigDecimal.ONE, "another", false, false, false);

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }
}

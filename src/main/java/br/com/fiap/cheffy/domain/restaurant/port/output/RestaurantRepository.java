package br.com.fiap.cheffy.domain.restaurant.port.output;

import br.com.fiap.cheffy.application.restaurant.dto.RestaurantCommandPort;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    boolean existsByNameAndCnpj(String restaurantName, String cnpj);
}

package br.com.fiap.cheffy.presentation.mapper;

import br.com.fiap.cheffy.application.restaurant.dto.RestaurantCommandPort;
import br.com.fiap.cheffy.presentation.dto.RestaurantAddressCreateDTO;
import br.com.fiap.cheffy.presentation.dto.RestaurantCreateDTO;
import org.junit.jupiter.api.Test;

import java.time.OffsetTime;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantWebMapperTest {

    private final RestaurantWebMapper mapper = new RestaurantWebMapper();

    @Test
    void toCommandMapsRestaurantAndAddressFields() {
        RestaurantCreateDTO dto = new RestaurantCreateDTO(
                "Restaurante Legal",
                "Italiana",
                "27865757000102",
                OffsetTime.parse("09:00:00-03:00"),
                OffsetTime.parse("18:00:00-03:00"),
                new RestaurantAddressCreateDTO(
                        "Rua A",
                        100,
                        "São Paulo",
                        "01001000",
                        "Centro",
                        "SP",
                        "Sala 10"
                )
        );

        RestaurantCommandPort command = mapper.toCommand(dto);

        assertThat(command.name()).isEqualTo(dto.name());
        assertThat(command.culinary()).isEqualTo(dto.culinary());
        assertThat(command.cnpj()).isEqualTo(dto.cnpj());
        assertThat(command.address().streetName()).isEqualTo(dto.address().streetName());
        assertThat(command.address().main()).isNull();
    }
}

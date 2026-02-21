package br.com.fiap.cheffy.presentation.dto;

import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.OffsetTime;

public record RestaurantCreateDTO(
        @NotBlank
        @Size(max = 255)
        String name,

        @NotBlank
        @Size(max = 255)
        String culinary,

        @NotBlank
        @CNPJ(message = "O CNPJ deve estar em um fomrato válido")
        String cnpj,

        @NotNull
        @JsonFormat(pattern = "HH:mm:ssXXX")
        OffsetTime openingTime,

        @NotNull
        @JsonFormat(pattern = "HH:mm:ssXXX")
        OffsetTime closingTime,

        @NotNull
        RestaurantAddressCreateDTO address
) {
}

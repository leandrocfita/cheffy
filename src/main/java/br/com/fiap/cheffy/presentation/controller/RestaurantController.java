package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.domain.restaurant.port.input.RegisterRestaurantInput;
import br.com.fiap.cheffy.presentation.dto.RestaurantCreateDTO;
import br.com.fiap.cheffy.presentation.dto.UserCreateDTO;
import br.com.fiap.cheffy.presentation.mapper.RestaurantWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    private RegisterRestaurantInput restaurantInput;
    private RestaurantWebMapper mapper;

    public RestaurantController(
            RegisterRestaurantInput restaurantInput,
            RestaurantWebMapper mapper
    ) {
        this.restaurantInput = restaurantInput;
        this.mapper = mapper;
    }

    @PostMapping("/{userId}")
    @Operation(
            summary = "Cadastra um novo restaurante para um usuário",
            description = "Cria um novo restaurante, tendo o usário enviado como proprietário. O usuário recebe o perfil de OWNER"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Resutarante criado com sucesso - Retorna UUID do novo restaurante",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "string", format = "uuid")
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou malformados"),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "409", description = "Estabelecimento já cadastrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<String> registerRestaurant(
            @RequestBody @Valid final RestaurantCreateDTO restaurantCreateDTO,
            @PathVariable @Valid final UUID userId
            ) {
        log.info("RestaurantController.createRestaurant - START - Create restaurante- user [{}]", userId);

        var createdId = restaurantInput.execute(mapper.toCommand(restaurantCreateDTO), userId);

        log.info("RestaurantController.createRestaurant - END - Restaurant created with id [{}]", createdId);

        MDC.clear();
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

}

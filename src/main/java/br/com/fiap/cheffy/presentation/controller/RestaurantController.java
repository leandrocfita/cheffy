package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.restaurant.dto.RestaurantQueryPort;
import br.com.fiap.cheffy.domain.restaurant.port.input.*;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultApiErrors;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultNotFoundApiResponse;
import br.com.fiap.cheffy.presentation.dto.RestaurantCreateDTO;
import br.com.fiap.cheffy.presentation.dto.RestaurantUpdateDTO;
import br.com.fiap.cheffy.presentation.exceptionhandler.model.Problem;
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

    private final RegisterRestaurantInput restaurantInput;
    private final DeactivateRestaurantInput deactivateRestaurantInput;
    private final ReactivateRestaurantInput reactivateRestaurantInput;
    private final UpdateRestaurantInput updateRestaurantInput;
    private final FindRestaurantByIdInput findRestaurantByIdInput;
    private final RestaurantWebMapper mapper;

    public RestaurantController(
            RegisterRestaurantInput restaurantInput,
            DeactivateRestaurantInput deactivateRestaurantInput,
            ReactivateRestaurantInput reactivateRestaurantInput,
            UpdateRestaurantInput updateRestaurantInput,
            FindRestaurantByIdInput findRestaurantByIdInput,
            RestaurantWebMapper mapper
    ) {
        this.restaurantInput = restaurantInput;
        this.deactivateRestaurantInput = deactivateRestaurantInput;
        this.reactivateRestaurantInput = reactivateRestaurantInput;
        this.updateRestaurantInput = updateRestaurantInput;
        this.findRestaurantByIdInput = findRestaurantByIdInput;
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

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Desativar restaurante de um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurante desativado com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID inválido - formato UUID incorreto"),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Void> deactivateRestaurant(@PathVariable @Valid final UUID id,
                                                     @RequestParam @Valid final UUID userId) {
        log.info("RestaurantController.deactivateRestaurant - START - Deactivate restaurant - id: [{}], userId: [{}]", id, userId);
        deactivateRestaurantInput.execute(id, userId);
        log.info("RestaurantController.deactivateRestaurant - END - Restaurant deactivated - id: [{}]", id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reactivate")
    @Operation(summary = "Reativar restaurante de um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurante reativado com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID inválido - formato UUID incorreto"),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Void> reactivateRestaurant(@PathVariable @Valid final UUID id,
                                                     @RequestParam @Valid final UUID userId) {
        log.info("RestaurantController.reactivateRestaurant - START - Reactivate restaurant - id: [{}], userId: [{}]", id, userId);
        reactivateRestaurantInput.execute(id, userId);
        log.info("RestaurantController.reactivateRestaurant - END - Restaurant reactivated - id: [{}]", id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Atualizar dados de um restaurante",
            description = "Atualiza os dados de um restaurante existente. Somente o proprietário pode atualizar." +
                    " Restaurantes desativados não podem ser atualizados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurante atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou malformados"),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
            @ApiResponse(responseCode = "409", description = "Operação não permitida"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Void> updateRestaurant(
            @PathVariable final UUID id,
            @RequestParam final UUID userId,
            @RequestBody @Valid final RestaurantUpdateDTO restaurantUpdateDTO
    ) {
        log.info("RestaurantController.updateRestaurant - START - Update restaurant - id: [{}], userId: [{}]", id, userId);
        updateRestaurantInput.execute(id, userId, mapper.toUpdateCommand(restaurantUpdateDTO));
        log.info("RestaurantController.updateRestaurant - END - Restaurant updated - id: [{}]", id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar restaurante por ID",
            description = "Retorna os dados completos de um restaurante com seu cardápio"
    )
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Restaurante encontrado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RestaurantQueryPort.class)
                    )
            ),
    })
    public ResponseEntity<RestaurantQueryPort> findRestaurantById(@PathVariable UUID id) {

        var user = findRestaurantByIdInput.execute(id);

        return ResponseEntity.ok(user);
    }


}

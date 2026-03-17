package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;
import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.port.input.CreateFoodItemInput;
import br.com.fiap.cheffy.domain.fooditem.port.input.UpdateFoodItemInput;
import br.com.fiap.cheffy.domain.fooditem.port.input.FindFoodItemByIdInput;
import br.com.fiap.cheffy.domain.fooditem.port.input.ListFoodItemsByRestaurantInput;
import br.com.fiap.cheffy.presentation.dto.FoodItemDTO;
import br.com.fiap.cheffy.presentation.dto.FoodItemUpdateDto;
import br.com.fiap.cheffy.presentation.mapper.FoodItemWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/restaurants/{restaurantId}/food-items", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Food Item", description = "Operações relacionadas ao food items")
public class FoodItemController {

    private final CreateFoodItemInput createFoodItemInput;
    private final UpdateFoodItemInput updateFoodItemInput;
    private final FindFoodItemByIdInput findFoodItemByIdInput;
    private final ListFoodItemsByRestaurantInput listFoodItemsByRestaurantInput;
    private final FoodItemWebMapper foodItemWebMapper;

    public FoodItemController(
            CreateFoodItemInput createFoodItemInput,
            ListFoodItemsByRestaurantInput listFoodItemsByRestaurantInput,
            FindFoodItemByIdInput findFoodItemByIdInput,
            FoodItemWebMapper foodItemWebMapper,
            UpdateFoodItemInput updateFoodItemInput
    ) {
        this.createFoodItemInput = createFoodItemInput;
        this.findFoodItemByIdInput = findFoodItemByIdInput;
        this.listFoodItemsByRestaurantInput = listFoodItemsByRestaurantInput;
        this.foodItemWebMapper = foodItemWebMapper;
        this.updateFoodItemInput = updateFoodItemInput;
    }

    @GetMapping
    @Operation(summary = "Listar food items do restaurante", description = "Retorna lista paginada de food items de um restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<PageResult<FoodItemQueryPort>> listFoodItemsByRestaurant(
            @PathVariable UUID restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "false") boolean includeInactive) {

        log.info("FoodItemController.listFoodItemsByRestaurant - START - restaurantId=[{}], page={}, size={}, includeInactive={}", restaurantId, page, size, includeInactive);

        PageRequest.SortDirection sortDirection = direction == Sort.Direction.DESC
                ? PageRequest.SortDirection.DESC
                : PageRequest.SortDirection.ASC;

        PageResult<FoodItemQueryPort> result = listFoodItemsByRestaurantInput.execute(restaurantId, PageRequest.of(page, size, sortBy, sortDirection), includeInactive);

        log.info("FoodItemController.listFoodItemsByRestaurant - END - Found [{}] items", result.numberOfElements());

        return ResponseEntity.ok(result);
    }

    @PostMapping()
    @Operation(summary = "Create a new food item", description = "Creates a new food item associated with a specific restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item criado no cardápio com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de cadastro do Item são inválidos"),
            @ApiResponse(responseCode = "404", description = "O restaurante em que o Item deve ser cadastrado não existe"),
            @ApiResponse(responseCode = "409", description = "O item ja existe no cardápio do restaurante"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Object> postFoodItem(
            @RequestBody @Valid FoodItemDTO foodItemDTO,
            @PathVariable @Valid UUID restaurantId){

        FoodItemCommandPort foodItemCommandPort = foodItemWebMapper.foodItemDtoToFoodItemCommandPort(foodItemDTO, restaurantId);

        FoodItem createdFoodItem = createFoodItemInput.execute(foodItemCommandPort);

        FoodItemQueryPort responseObject = foodItemWebMapper.foodItemToFoodItemQueryPort(createdFoodItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
    }
    @PutMapping("/{foodItemId}")
    @Operation(summary = "Update an existing food item", description = "Updates an existing food item associated with a specific restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de atualização do Item são inválidos"),
            @ApiResponse(responseCode = "403", description = "Operação não permitida. O usuário não tem permissão para alterar este restaurante"),
            @ApiResponse(responseCode = "404", description = "Restaurante ou Item não encontrado"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Object> updateFoodItem(
            @RequestBody @Valid FoodItemUpdateDto foodItemUpdateDTO,
            @PathVariable @Valid UUID restaurantId,
            @RequestAttribute("userId") UUID userId,
            @PathVariable @Valid UUID foodItemId
    ){
        FoodItemCommandPort foodItemCommandPort = foodItemWebMapper.foodItemDtoToFoodItemCommandPort(foodItemUpdateDTO, restaurantId);

        updateFoodItemInput.update(foodItemId, restaurantId, userId, foodItemCommandPort);

        return ResponseEntity.status(HttpStatusCode.valueOf(204)).body(null);
    }


    @Transactional(readOnly = true)
    @GetMapping("/{foodItemId}")
    @Operation(
            summary = "Buscar item do cardápio por ID",
            description = "Retorna os dados completos de um item específico do cardápio de um restaurante"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Item do cardápio encontrado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(responseCode = "400", description = "ID inválido - formato UUID incorreto"),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Item do cardápio não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<FoodItemQueryPort> getFoodItemById(@PathVariable UUID restaurantId, @PathVariable UUID foodItemId) {


        log.info("FoodItemController.getFoodItemById - START - Finding food item [{}] for restaurant [{}]", foodItemId, restaurantId);

        FoodItemQueryPort foodItemQueryPort = findFoodItemByIdInput.execute(restaurantId, foodItemId);

        log.info("FoodItemController.getFoodItemById - END - Food item found [{}]", foodItemId);
        return ResponseEntity.ok(foodItemQueryPort);
    }
}

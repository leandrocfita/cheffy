package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;
import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.port.input.CreateFoodItemInput;
import br.com.fiap.cheffy.presentation.dto.FoodItemDTO;
import br.com.fiap.cheffy.presentation.mapper.FoodItemWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final FoodItemWebMapper foodItemWebMapper;

    public FoodItemController(CreateFoodItemInput createFoodItemInput, FoodItemWebMapper foodItemWebMapper){
        this.createFoodItemInput= createFoodItemInput;
        this.foodItemWebMapper = foodItemWebMapper;
    }

    @Transactional
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

        FoodItemCommandPort foodItemQueryPort = foodItemWebMapper.foodItemDtoToFoodItemCommandPort(foodItemDTO, restaurantId);

        FoodItem createdFoodItem = createFoodItemInput.execute(foodItemQueryPort);

        FoodItemQueryPort responseObject = foodItemWebMapper.foodItemToFoodItemQueryPort(createdFoodItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
    }
}

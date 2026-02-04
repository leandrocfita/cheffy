package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.domain.user.port.input.CreateUserInput;
import br.com.fiap.cheffy.presentation.dto.UserCreateDTO;
import br.com.fiap.cheffy.presentation.mapper.UserWebMapper;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final CreateUserInput createUserInput;
    private final UserWebMapper mapper;



    public UserController(
            UserWebMapper mapper,
            CreateUserInput createUserInput)
    {
        this.mapper = mapper;
        this.createUserInput = createUserInput;
    }

    @PostMapping
    @Operation(
            summary = "Criar novo usuário",
            description = "Cadastra novo usuário"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário criado com sucesso - Retorna UUID do novo usuário",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "string", format = "uuid")
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou malformados"),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "409", description = "Conflito - Email ou Login já cadastrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<String> createTbUser(@RequestBody @Valid final UserCreateDTO userCreateDTO) {
        log.info("UserController.createTbUser - START - Create user");
        var createdId = createUserInput.execute(mapper.toCommand(userCreateDTO));
        log.info("UserController.createTbUser - END - User created with id [{}]", createdId);
        MDC.clear();
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }
}

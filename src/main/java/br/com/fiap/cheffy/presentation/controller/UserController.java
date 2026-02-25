package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.domain.user.port.input.*;
import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.presentation.dto.AddressCreateDTO;
import br.com.fiap.cheffy.presentation.dto.AddressPatchDTO;
import br.com.fiap.cheffy.presentation.dto.UserCreateDTO;
import br.com.fiap.cheffy.presentation.dto.UserUpdatePasswordDTO;
import br.com.fiap.cheffy.presentation.dto.UserUpdateDTO;
import br.com.fiap.cheffy.presentation.mapper.UserWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final CreateUserInput createUserInput;
    private final DeactivateUserInput deactivateUserInput;
    private final ReactivateUserInput reactivateUserInput;
    private final UpdateUserPasswordInput updateUserPasswordInput;
    private final UpdateUserInput updateUserInput;
    private final AddAddressInput addAddressInput;
    private final UpdateAddressInput updateAddressInput;
    private final RemoveAddressInput removeAddress;
    private final ListAllUsersInput listAllUsersInput;
    private final FindUserByIdInput findUserByIdInput;
    private final UserWebMapper mapper;



    public UserController(
            UserWebMapper mapper,
            CreateUserInput createUserInput,
            DeactivateUserInput deactivateUserInput,
            ReactivateUserInput reactivateUserInput,
            UpdateUserPasswordInput updateUserPasswordInput,
            UpdateUserInput updateUserInput,
            AddAddressInput addAddressInput,
            UpdateAddressInput updateAddressInput,
            RemoveAddressInput removeAddress,
            ListAllUsersInput listAllUsersInput,
            FindUserByIdInput findUserByIdInput)
    {
        this.updateUserPasswordInput = updateUserPasswordInput;
        this.createUserInput = createUserInput;
        this.deactivateUserInput = deactivateUserInput;
        this.reactivateUserInput = reactivateUserInput;
        this.updateUserInput = updateUserInput;
        this.mapper = mapper;
        this.updateAddressInput = updateAddressInput;
        this.addAddressInput = addAddressInput;
        this.removeAddress = removeAddress;
        this.listAllUsersInput = listAllUsersInput;
        this.findUserByIdInput = findUserByIdInput;
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
    public ResponseEntity<String> createUser(@RequestBody @Valid final UserCreateDTO userCreateDTO) {
        log.info("UserController.createTbUser - START - Create user");
        var createdId = createUserInput.execute(mapper.toCommand(userCreateDTO));
        log.info("UserController.createTbUser - END - User created with id [{}]", createdId);
        MDC.clear();
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Atualizar senha do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Nova senha inválida"),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<UUID> updateUserPassword(@PathVariable final UUID id,
                                                   @RequestBody @Valid final UserUpdatePasswordDTO userUpdatePasswordDTO) {
        log.info("UserController.updateUserPassword - START - Update password for user [{}]", id);
        updateUserPasswordInput.execute(mapper.toCommand(userUpdatePasswordDTO), id);
        log.info("UserController.updateUserPassword - END - Password updated for user [{}]", id);
        return ResponseEntity.ok(id);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Atualizar usuário",
            description = "Atualização parcial - apenas campos enviados são modificados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de atualização inválidos"),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito - Email já cadastrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Void> updateUser(@PathVariable final UUID id,
                                           @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        log.info("UserController.updateUser - START - Update user");
        updateUserInput.execute(id, mapper.toCommand(userUpdateDTO));
        log.info("UserController.updateUser - END - User updated [{}]", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Desativar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário desativado com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID inválido - formato UUID incorreto"),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Void> deactivateUser(@PathVariable final UUID id) {
        log.info("UserController.deactivateUser - START - Deactivate user");
        deactivateUserInput.execute(id);
        log.info("UserController.deactivateUser - END - User deactivated [{}]", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reactivate")
    @Operation(summary = "Reativa usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário reativado com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID inválido - formato UUID incorreto"),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Void> reactivateUser(@PathVariable final UUID id) {
        log.info("UserController.reactivateUser - START - Reactivate user");
        reactivateUserInput.execute(id);
        log.info("UserController.reactivateUser - END - User reactivated [{}]", id);
        return ResponseEntity.noContent().build();
    }

    //ADDRESSES
    @PostMapping("/{userId}/addresses")
    @Operation(summary = "Adicionar novo endereço ao usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Long> addAddress(
            @PathVariable UUID userId,
            @RequestBody @Valid AddressCreateDTO dto) {

        log.info("UserController.addAddress - START - User [{}]", userId);

        addAddressInput.execute(mapper.toCommand(dto), userId);

        log.info("UserController.addAddress - END");
        MDC.clear();

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{userId}/addresses/{addressId}")
    @Operation(summary = "Atualizar parcialmente um endereço do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Endereço atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário ou endereço não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Void> updateAddress(
            @PathVariable UUID userId,
            @PathVariable Long addressId,
            @RequestBody @Valid AddressPatchDTO dto) {

        log.info("UserController.updateAddress - START - User [{}] Address [{}]", userId, addressId);

        updateAddressInput.execute(userId, addressId, mapper.toCommand(dto));

        log.info("UserController.updateAddress - END");
        MDC.clear();

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/addresses/{addressId}")
    @Operation(summary = "Remover endereço do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Endereço removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário ou endereço não encontrado"),
            @ApiResponse(responseCode = "400", description = "Operação não permitida"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Void> removeAddress(
            @PathVariable UUID userId,
            @PathVariable Long addressId) {

        log.info("UserController.removeAddress - START - User [{}] Address [{}]", userId, addressId);

        removeAddress.execute(userId, addressId);

        log.info("UserController.removeAddress - END");
        MDC.clear();

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(
            summary = "Listar todos os usuários",
            description = "Retorna lista paginada de todos os usuários cadastrados"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuários retornada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<?> listAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {

        log.info("UserController.listAllUsers - START - Listing users [page={}, size={}, sortBy={}, direction={}]", page, size, sortBy, direction);

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<UserQueryPort> users = listAllUsersInput.execute(pageable);

        log.info("UserController.listAllUsers - END - Found [{}] users in page [{}]", users.getNumberOfElements(), page);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados completos de um usuário específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário encontrado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<?> findUserById(@PathVariable UUID id) {
        log.info("UserController.findUserById - START - Finding user [{}]", id);

        var user = findUserByIdInput.execute(id);

        log.info("UserController.findUserById - END - User found [{}]", id);
        MDC.clear();

        return ResponseEntity.ok(user);
    }
}

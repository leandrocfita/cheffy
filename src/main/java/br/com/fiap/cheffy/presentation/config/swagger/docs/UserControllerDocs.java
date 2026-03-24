package br.com.fiap.cheffy.presentation.config.swagger.docs;

import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultApiErrors;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultBadRequestApiResponse;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultConflictApiResponse;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultNotFoundApiResponse;
import br.com.fiap.cheffy.presentation.config.swagger.schema.UserPageResponseSchema;
import br.com.fiap.cheffy.presentation.dto.AddressCreateDTO;
import br.com.fiap.cheffy.presentation.dto.AddressPatchDTO;
import br.com.fiap.cheffy.presentation.dto.UserCreateDTO;
import br.com.fiap.cheffy.presentation.dto.UserUpdateDTO;
import br.com.fiap.cheffy.presentation.dto.UserUpdatePasswordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "2. Users", description = "Cadastro, consulta e manutenção de usuários")
public interface UserControllerDocs {

    @Operation(summary = "Criar novo usuário", description = "Cadastra novo usuário")
    @ApiResponse(
            responseCode = "201",
            description = "Usuário criado com sucesso - Retorna UUID do novo usuário",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(type = "string", format = "uuid")
            )
    )
    @DefaultBadRequestApiResponse
    @DefaultConflictApiResponse
    @ApiResponse(responseCode = "500", description = "Erro interno")
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserCreateDTO.class),
                    examples = @ExampleObject(
                            name = "Create User Request",
                            value = """
                                    {
                                      "name": "Joao Silva",
                                      "email": "joao.silva@example.com",
                                      "login": "joao.silva",
                                      "password": "SenhaForte@123",
                                    
                                      "address": {
                                        "streetName": "Rua das Flores",
                                        "number": 123,
                                        "city": "Sao Paulo",
                                        "postalCode": "01001000",
                                        "neighborhood": "Centro",
                                        "stateProvince": "SP",
                                        "addressLine": "Apartamento 45",
                                        "main": true
                                      }
                                    }
                                    """
                    )
            )
    )
    ResponseEntity<String> createUser(@RequestBody @Valid UserCreateDTO userCreateDTO);

    @Operation(summary = "Atualizar senha do usuário")
    @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    ResponseEntity<UUID> updateUserPassword(@PathVariable final UUID id,
                                            @RequestBody @Valid final UserUpdatePasswordDTO userUpdatePasswordDTO);

    @Operation(summary = "Atualizar usuário", description = "Atualização parcial - apenas campos enviados são modificados")
    @ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    @DefaultConflictApiResponse
    ResponseEntity<Void> updateUser(@PathVariable final UUID id,
                                    @RequestBody @Valid UserUpdateDTO userUpdateDTO);

    @Operation(summary = "Desativar usuário")
    @ApiResponse(responseCode = "204", description = "Usuário desativado com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    ResponseEntity<Void> deactivateUser(@PathVariable final UUID id);

    @Operation(summary = "Reativar usuário")
    @ApiResponse(responseCode = "204", description = "Usuário reativado com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    ResponseEntity<Void> reactivateUser(@PathVariable final UUID id);

    @Operation(summary = "Adicionar novo endereço ao usuário")
    @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    ResponseEntity<Long> addAddress(@PathVariable UUID userId,
                                    @RequestBody @Valid AddressCreateDTO dto);

    @Operation(summary = "Atualizar parcialmente um endereço do usuário")
    @ApiResponse(responseCode = "204", description = "Endereço atualizado com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    ResponseEntity<Void> updateAddress( @PathVariable UUID userId,
                                        @PathVariable Long addressId,
                                        @RequestBody @Valid AddressPatchDTO dto);

    @Operation(summary = "Remover endereço do usuário")
    @ApiResponse(responseCode = "204", description = "Endereço removido com sucesso")
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    ResponseEntity<Void> removeAddress(@PathVariable UUID userId,
                                       @PathVariable Long addressId);

    @Operation(summary = "Listar todos os usuários", description = "Retorna lista paginada de todos os usuários cadastrados")
    @ApiResponse(
            responseCode = "200",
            description = "Lista de usuários retornada com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserPageResponseSchema.class)
            )
    )
    @DefaultApiErrors
    ResponseEntity<PageResult<UserQueryPort>> listAllUsers(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "name") String sortBy,
                                                           @RequestParam(defaultValue = "ASC") Sort.Direction direction);

    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados completos de um usuário específico")
    @ApiResponse(
            responseCode = "200",
            description = "Usuário encontrado com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserQueryPort.class)
            )
    )
    @DefaultApiErrors
    @DefaultNotFoundApiResponse
    ResponseEntity<UserQueryPort> findUserById(@PathVariable UUID id);

    @Operation(summary = "Buscar usuários por nome", description = "Retorna lista paginada de usuários filtrados pelo nome")
    @ApiResponse(
            responseCode = "200",
            description = "Usuários encontrados com sucesso",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserPageResponseSchema.class)
            )
    )
    @DefaultBadRequestApiResponse
    @DefaultApiErrors
    ResponseEntity<PageResult<UserQueryPort>> searchUsersByName( @RequestParam(name = "name") @NotBlank String name,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size,
                                                                 @RequestParam(defaultValue = "name") String sortBy,
                                                                 @RequestParam(defaultValue = "ASC") Sort.Direction direction);
}

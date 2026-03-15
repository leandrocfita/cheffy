package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;
import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.domain.profile.port.input.FindProfileByInput;
import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.profile.port.input.ListAllProfilesInput;
import br.com.fiap.cheffy.domain.profile.port.input.ProfileCreateInput;
import br.com.fiap.cheffy.domain.profile.port.input.ProfileUpdateInput;
import br.com.fiap.cheffy.presentation.dto.ProfileCreateReponseDto;
import br.com.fiap.cheffy.presentation.dto.ProfileInputDto;
import br.com.fiap.cheffy.presentation.mapper.ProfileWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profile", description = "Operações relacionadas aos perfis de usuário")
public class ProfileController {

    private final ProfileCreateInput profileCreateInput;
    private final ProfileUpdateInput profileUpdateInput;
    private final FindProfileByInput findProfileByIdInput;
    private final ListAllProfilesInput listAllProfilesInput;

    public ProfileController(
            ProfileCreateInput profileCreateInput,
            ProfileUpdateInput profileUpdateInput,
            ListAllProfilesInput listAllProfilesInput,
            FindProfileByInput findProfileByIdInput) {
        this.profileCreateInput = profileCreateInput;
        this.profileUpdateInput = profileUpdateInput;
        this.findProfileByIdInput = findProfileByIdInput;
        this.listAllProfilesInput = listAllProfilesInput;
    }

    @PostMapping("")
    @Operation(
            summary = "Criar novo perfil",
            description = "Criar um novo perfil de usuário com base no tipo fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Perfil já existente, duplicatas não são aceitas"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Object> createProfile(@RequestBody @Valid ProfileInputDto profileInputDto) {

        ProfileInputPort profileInputPort = ProfileWebMapper.toProfileInputCommandPort(profileInputDto);
        Long id = profileCreateInput.create(profileInputPort);

        ProfileCreateReponseDto profileCreateReponseDto = new ProfileCreateReponseDto(id, profileInputDto.profileNameType(), "Profile created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(profileCreateReponseDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar perfil por ID",
            description = "Atualiza um tipo de perfil existente identificado pelo seu ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Perfil atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> updateProfileById(@PathVariable Long id, @RequestBody @Valid ProfileInputDto profileInputDto) {
        ProfileInputPort profileInputPort = ProfileWebMapper.toProfileInputCommandPort(profileInputDto);
        profileUpdateInput.updateById(id, profileInputPort);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/name/{name}")
    @Operation(
            summary = "Atualizar perfil por nome",
            description = "Atualiza um tipo de perfil existente identificado pelo seu nome"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Perfil atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> updateProfileByName(@PathVariable String name, @RequestBody @Valid ProfileInputDto profileInputDto) {
        ProfileInputPort profileInputPort = ProfileWebMapper.toProfileInputCommandPort(profileInputDto);
        profileUpdateInput.updateByName(name, profileInputPort);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar perfil por ID",
            description = "Retorna os dados completos de um perfil específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Perfil encontrado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            ),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<ProfileQueryPort> findProfileById(@PathVariable Long id) {
        log.info("ProfileController.findProfileById - START - Finding profile by ID [{}]", id);

        var profile = findProfileByIdInput.execute(id);

        log.info("ProfileController.findProfileById - END - Profile found: [{}]", profile);
        MDC.clear();

        return ResponseEntity.ok(profile);
    }

    @GetMapping
    @Operation(summary = "Listar todos os perfis")
    @ApiResponse(responseCode = "200", description = "Lista de perfis retornada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    public ResponseEntity<PageResult<ProfileQueryPort>> listAllProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "type") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        log.info("ProfileController.listAllProfiles - START - Listing profiles [page={}, size={}, sortBy={}, direction={}]", page, size, sortBy, direction);

        PageRequest.SortDirection sortDirection = direction == Sort.Direction.DESC
                ? PageRequest.SortDirection.DESC
                : PageRequest.SortDirection.ASC;

        PageRequest pageRequest = PageRequest.of(page, size, sortBy, sortDirection);

        PageResult<ProfileQueryPort> profiles = listAllProfilesInput.execute(pageRequest);

        log.info("ProfileController.listAllProfiles - END - Found [{}] profiles in page [{}]", profiles.numberOfElements(), page);

        return ResponseEntity.ok(profiles);
    }
}

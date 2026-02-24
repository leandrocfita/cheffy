package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;
import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.domain.profile.exception.ProfileAlreadyExistException;
import br.com.fiap.cheffy.domain.profile.port.input.ListAllProfilesInput;
import br.com.fiap.cheffy.domain.profile.port.input.ProfileCreateInput;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profile", description = "Operations related to user profiles")
public class ProfileController {

    private final ProfileCreateInput profileCreateInput;
    private final ListAllProfilesInput listAllProfilesInput;

    public ProfileController(
            ProfileCreateInput profileCreateInput,
            ListAllProfilesInput listAllProfilesInput) {
        this.profileCreateInput = profileCreateInput;
        this.listAllProfilesInput = listAllProfilesInput;
    }

    @PostMapping("")
    @Operation(summary = "Create a new profile", description = "Creates a new user profile based on the provided type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Profile already exists, no duplicatas wil be accepted"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Object> createProfile(@RequestBody @Valid ProfileInputDto profileInputDto) {

            ProfileInputPort profileInputPort = ProfileWebMapper.toProfileInputCommandPort(profileInputDto);
            Long id = profileCreateInput.create(profileInputPort);

            ProfileCreateReponseDto profileCreateReponseDto = new ProfileCreateReponseDto(id, profileInputDto.profileNameType(), "Profile created successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(profileCreateReponseDto);
    }

    @GetMapping
    @Operation(summary = "Listar todos os perfis")
    @ApiResponse(responseCode = "200", description = "Lista de perfis retornada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    public ResponseEntity<?> listAllProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "type") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        log.info("ProfileController.listAllProfiles - START - Listing profiles [page={}, size={}, sortBy={}, direction={}]", page, size, sortBy, direction);

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ProfileQueryPort> profiles = listAllProfilesInput.execute(pageable);

        log.info("ProfileController.listAllProfiles - END - Found [{}] profiles in page [{}]", profiles.getNumberOfElements(), page);

        return ResponseEntity.ok(profiles);
    }
}

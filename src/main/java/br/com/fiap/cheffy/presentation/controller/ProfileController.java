package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;
import br.com.fiap.cheffy.domain.profile.exception.ProfileAlreadyExistException;
import br.com.fiap.cheffy.domain.profile.port.input.ProfileCreateInput;
import br.com.fiap.cheffy.presentation.dto.ProfileCreateReponseDto;
import br.com.fiap.cheffy.presentation.dto.ProfileInputDto;
import br.com.fiap.cheffy.presentation.mapper.ProfileWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profile", description = "Operations related to user profiles")
public class ProfileController {

    private final ProfileCreateInput profileCreateInput;

    public ProfileController(ProfileCreateInput profileCreateInput) {
        this.profileCreateInput = profileCreateInput;
    }

    @PostMapping("/")
    @Operation(summary = "Create a new profile", description = "Creates a new user profile based on the provided type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Profile already exists, no duplicatas wil be accepted"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Object> createProfile(@RequestBody @Valid ProfileInputDto profileInputDto) {

        try {

            ProfileInputPort profileInputPort = ProfileWebMapper.toProfileInputCommandPort(profileInputDto);
            Long id = profileCreateInput.create(profileInputPort);

            ProfileCreateReponseDto profileCreateReponseDto = new ProfileCreateReponseDto(id, profileInputDto.profileNameType(), "Profile created successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(profileCreateReponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ProfileAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

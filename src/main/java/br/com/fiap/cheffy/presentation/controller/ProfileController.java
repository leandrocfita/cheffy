package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputDto;
import br.com.fiap.cheffy.domain.profile.port.input.IProfileCreateInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping(value = "/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profile", description = "Operations related to user profiles")
public class ProfileController {

    private final IProfileCreateInput profileCreateInput;

    public ProfileController(IProfileCreateInput profileCreateInput) {
        this.profileCreateInput = profileCreateInput;
    }

    @PostMapping("/")
    @Operation(summary = "Create a new profile", description = "Creates a new user profile based on the provided type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Long> createProfile(@RequestBody ProfileInputDto profileInputDto, UriComponentsBuilder uriBuilder) {
        Long id = profileCreateInput.create(profileInputDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
}

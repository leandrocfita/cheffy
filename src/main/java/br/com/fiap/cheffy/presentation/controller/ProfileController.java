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
import br.com.fiap.cheffy.presentation.config.swagger.docs.ProfileControllerDocs;
import br.com.fiap.cheffy.presentation.dto.ProfileCreateReponseDto;
import br.com.fiap.cheffy.presentation.dto.ProfileInputDto;
import br.com.fiap.cheffy.presentation.mapper.ProfileWebMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController implements ProfileControllerDocs {

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

    @Override
    @PostMapping("")
    public ResponseEntity<ProfileCreateReponseDto> createProfile(@RequestBody @Valid ProfileInputDto profileInputDto) {

        ProfileInputPort profileInputPort = ProfileWebMapper.toProfileInputCommandPort(profileInputDto);
        Long id = profileCreateInput.create(profileInputPort);

        ProfileCreateReponseDto profileCreateReponseDto = new ProfileCreateReponseDto(id, profileInputDto.profileNameType(), "Profile created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(profileCreateReponseDto);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProfileById(@PathVariable Long id, @RequestBody @Valid ProfileInputDto profileInputDto) {
        ProfileInputPort profileInputPort = ProfileWebMapper.toProfileInputCommandPort(profileInputDto);
        profileUpdateInput.updateById(id, profileInputPort);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/name/{name}")
    public ResponseEntity<Void> updateProfileByName(@PathVariable String name, @RequestBody @Valid ProfileInputDto profileInputDto) {
        ProfileInputPort profileInputPort = ProfileWebMapper.toProfileInputCommandPort(profileInputDto);
        profileUpdateInput.updateByName(name, profileInputPort);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProfileQueryPort> findProfileById(@PathVariable Long id) {
        try {
            log.info("ProfileController.findProfileById - START - Finding profile by ID [{}]", id);
            var profile = findProfileByIdInput.execute(id);
            log.info("ProfileController.findProfileById - END - Profile found: [{}]", profile);
            return ResponseEntity.ok(profile);
        } finally {
            MDC.clear();
        }
    }

    @Override
    @GetMapping
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

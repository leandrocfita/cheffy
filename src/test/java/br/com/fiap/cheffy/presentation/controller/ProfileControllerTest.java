package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;
import br.com.fiap.cheffy.domain.profile.port.input.ProfileCreateInput;
import br.com.fiap.cheffy.domain.profile.port.input.ProfileUpdateInput;
import br.com.fiap.cheffy.presentation.dto.ProfileCreateReponseDto;
import br.com.fiap.cheffy.presentation.dto.ProfileInputDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private ProfileCreateInput profileCreateInput;

    @Mock
    private ProfileUpdateInput profileUpdateInput;

    @InjectMocks
    private ProfileController profileController;

    @Test
    @DisplayName("Should return 201 Created when profile is created successfully")
    void createProfileReturnsCreated() {
        ProfileInputDto inputDto = new ProfileInputDto("Chef");
        Long createdId = 1L;
        when(profileCreateInput.create(any(ProfileInputPort.class))).thenReturn(createdId);
        ProfileInputDto dto = new ProfileInputDto("CLIENT");
        when(profileCreateInput.create(any(ProfileInputPort.class))).thenReturn(1L);

        ResponseEntity<Object> response = profileController.createProfile(inputDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isInstanceOf(ProfileCreateReponseDto.class);
        ProfileCreateReponseDto responseBody = (ProfileCreateReponseDto) response.getBody();
        Assertions.assertNotNull(responseBody);
        assertThat(responseBody.id()).isEqualTo(createdId);
        assertThat(responseBody.nameType()).isEqualTo("Chef");
    }

    @Test
    @DisplayName("Should return 204 No Content when profile is updated by ID successfully")
    void updateProfileByIdReturnsNoContent() {
        Long id = 1L;
        ProfileInputDto inputDto = new ProfileInputDto("Chef");

        ResponseEntity<Void> response = profileController.updateProfileById(id, inputDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(profileUpdateInput).updateById(eq(id), any(ProfileInputPort.class));
    }

    @Test
    @DisplayName("Should return 204 No Content when profile is updated by Name successfully")
    void updateProfileByNameReturnsNoContent() {
        String name = "Client";
        ProfileInputDto inputDto = new ProfileInputDto("Chef");

        ResponseEntity<Void> response = profileController.updateProfileByName(name, inputDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(profileUpdateInput).updateByName(eq(name), any(ProfileInputPort.class));
        var body =  response.getBody();
        assertThat(body).isNull();
    }
}

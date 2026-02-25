package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;
import br.com.fiap.cheffy.domain.profile.port.input.ProfileCreateInput;
import br.com.fiap.cheffy.presentation.dto.ProfileCreateReponseDto;
import br.com.fiap.cheffy.presentation.dto.ProfileInputDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private ProfileCreateInput profileCreateInput;

    @InjectMocks
    private ProfileController profileController;

    @Test
    void createProfileReturnsCreated() {
        ProfileInputDto dto = new ProfileInputDto("CLIENT");
        when(profileCreateInput.create(any(ProfileInputPort.class))).thenReturn(1L);

        ResponseEntity<Object> response = profileController.createProfile(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ProfileCreateReponseDto body = (ProfileCreateReponseDto) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(1L);
        assertThat(body.nameType()).isEqualTo("CLIENT");
    }
}

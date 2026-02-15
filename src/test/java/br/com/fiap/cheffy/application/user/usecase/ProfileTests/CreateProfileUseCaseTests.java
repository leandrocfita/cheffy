package br.com.fiap.cheffy.application.user.usecase.ProfileTests;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputDto;
import br.com.fiap.cheffy.application.profile.usecase.CreateProfileUseCase;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateProfileUseCaseTests {

    @Mock
    private ProfileRepository profileGateway;

    @InjectMocks
    private CreateProfileUseCase profileCreateUseCase;

    @Test
    @DisplayName("Should create profile successfully when input is valid")
    void shouldCreateProfileSuccessfully() {
        // Given
        ProfileInputDto inputDto = new ProfileInputDto("Chef");
        // Assuming the gateway returns a persisted profile with ID 1
        Profile savedProfile = Profile.create(1L, "Chef");

        when(profileGateway.save(any(Profile.class))).thenReturn(savedProfile.getId());

        // When
        Long resultId = profileCreateUseCase.create(inputDto);

        // Then
        assertNotNull(resultId);
        assertEquals(1L, resultId);
        verify(profileGateway, times(1)).save(any(Profile.class));
    }

    @Test
    @DisplayName("Should throw exception when gateway fails")
    void shouldThrowExceptionWhenGatewayFails() {
        ProfileInputDto inputDto = new ProfileInputDto("Chef");
        when(profileGateway.save(any(Profile.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> profileCreateUseCase.create(inputDto));
    }
}

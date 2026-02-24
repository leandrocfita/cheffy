package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.application.profile.mapper.ProfileQueryMapper;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListAllProfileUseCaseTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileQueryMapper mapper;

    private ListAllProfileUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListAllProfileUseCase(profileRepository, mapper);
    }

    @Test
    void shouldListProfilesSuccessfully() {
        Profile firstProfile = createProfile(1L, ProfileType.CLIENT.getType());
        Profile secondProfile = createProfile(2L, ProfileType.OWNER.getType());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Profile> profilePage = new PageImpl<>(Arrays.asList(firstProfile, secondProfile), pageable, 2);

        ProfileQueryPort mappedFirstProfile = mock(ProfileQueryPort.class);
        ProfileQueryPort mappedSecondProfile = mock(ProfileQueryPort.class);

        when(profileRepository.findAll(pageable)).thenReturn(profilePage);
        when(mapper.toQuery(firstProfile)).thenReturn(mappedFirstProfile);
        when(mapper.toQuery(secondProfile)).thenReturn(mappedSecondProfile);

        Page<ProfileQueryPort> profilesPageResult = useCase.execute(pageable);

        assertNotNull(profilesPageResult);
        assertEquals(2, profilesPageResult.getTotalElements());
        assertEquals(2, profilesPageResult.getContent().size());
        verify(profileRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldReturnEmptyListWhenNoProfilesFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Profile> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(profileRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<ProfileQueryPort> profilesPageResult = useCase.execute(pageable);

        assertNotNull(profilesPageResult);
        assertEquals(0, profilesPageResult.getTotalElements());
        assertTrue(profilesPageResult.getContent().isEmpty());
    }

    @Test
    void shouldHandlePagination() {
        Profile profile = createProfile(1L, ProfileType.CLIENT.getType());

        Pageable pageable = PageRequest.of(1, 5);
        Page<Profile> profilePage = new PageImpl<>(List.of(profile), pageable, 10);

        ProfileQueryPort query = mock(ProfileQueryPort.class);
        when(profileRepository.findAll(pageable)).thenReturn(profilePage);
        when(mapper.toQuery(profile)).thenReturn(query);

        Page<ProfileQueryPort> result = useCase.execute(pageable);

        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals(1, result.getNumber());
    }

    private Profile createProfile(Long id, String type) {
        return Profile.create(id, type);
    }
}
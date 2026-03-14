package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.dto.PageInputPort;
import br.com.fiap.cheffy.application.profile.dto.PageOutputPort;
import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.application.profile.dto.SortRequestPort;
import br.com.fiap.cheffy.application.profile.mapper.ProfileQueryMapper;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private int page;
    private int size;
    private SortRequestPort sort;

    private Profile firstProfile;
    private Profile secondProfile;

    private ProfileQueryPort mappedFirstProfile;
    private ProfileQueryPort mappedSecondProfile;

    private List<Profile> profiles;

    private PageInputPort pageInput;

    @BeforeEach
    void setUp() {
        useCase = new ListAllProfileUseCase(profileRepository, mapper);

        this.page = 0;
        this.size = 10;
        this.sort = new SortRequestPort("type", SortRequestPort.Direction.ASC);

        this.firstProfile = createProfile(1L, ProfileType.CLIENT.getType());
        this.secondProfile = createProfile(2L, ProfileType.OWNER.getType());

        this.mappedFirstProfile = new ProfileQueryPort(1L, ProfileType.CLIENT.getType());
        this.mappedSecondProfile = new ProfileQueryPort(2L, ProfileType.OWNER.getType());

        this.profiles = Arrays.asList(this.firstProfile, this.secondProfile);

        pageInput = new PageInputPort(this.page, this.size, this.sort);
    }

    @Test
    void shouldListProfilesSuccessfully() {

        var profilePage = new PageOutputPort<Profile>(profiles, this.page, this.size, 2);

        when(profileRepository.findAll(this.pageInput)).thenReturn(profilePage);

        when(mapper.toQuery(this.firstProfile)).thenReturn(this.mappedFirstProfile);
        when(mapper.toQuery(this.secondProfile)).thenReturn(this.mappedSecondProfile);

        var result = useCase.execute(this.pageInput);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        verify(profileRepository, times(1)).findAll(this.pageInput);
        assertEquals(ProfileType.CLIENT.getType(), result.getContent().getFirst().type());
    }

    @Test
    void shouldListProfilesSuccessfullyWithDescending() {
        var mappedFirstProfileExpected = new ProfileQueryPort(2L, ProfileType.OWNER.getType());
        var mappedSecondProfileExpected = new ProfileQueryPort(1L, ProfileType.CLIENT.getType());

        var profilePage = new PageOutputPort<Profile>(Arrays.asList(this.firstProfile, this.secondProfile), this.page, this.size, 2);

        when(profileRepository.findAll(this.pageInput)).thenReturn(profilePage);
        when(mapper.toQuery(any())).thenReturn(mappedFirstProfileExpected, mappedSecondProfileExpected);

        var result = useCase.execute(this.pageInput);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        verify(profileRepository, times(1)).findAll(this.pageInput);
        assertEquals(ProfileType.OWNER.getType(), result.getContent().getFirst().type());
    }

    @Test
    void shouldReturnEmptyListWhenNoProfilesFound() {
        var emptyPage = new PageOutputPort<Profile>(Collections.emptyList(), this.page, this.size, 0);

        when(profileRepository.findAll(this.pageInput)).thenReturn(emptyPage);

        var result = useCase.execute(this.pageInput);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    private Profile createProfile(Long id, String type) {
        return Profile.create(id, type);
    }
}
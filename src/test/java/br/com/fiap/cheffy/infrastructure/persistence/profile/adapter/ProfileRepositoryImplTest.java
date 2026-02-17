package br.com.fiap.cheffy.infrastructure.persistence.profile.adapter;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.infrastructure.persistence.profile.entity.ProfileJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.profile.mapper.ProfilePersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.profile.repository.ProfileJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileRepositoryImplTest {

    @Mock
    private ProfileJpaRepository jpaRepository;

    @Mock
    private ProfilePersistenceMapper mapper;

    @InjectMocks
    private ProfileRepositoryImpl profileRepository;

    @Test
    void findById() {
        Profile profile = new Profile(1L, ProfileType.CLIENT.getType());
        ProfileJpaEntity jpaEntity = new ProfileJpaEntity();
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(jpaEntity));
        when(mapper.toDomain(jpaEntity)).thenReturn(profile);

        Optional<Profile> result = profileRepository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(profile);
    }

    @Test
    void findByType() {
        Profile profile = new Profile(1L, ProfileType.CLIENT.getType());
        ProfileJpaEntity jpaEntity = new ProfileJpaEntity();
        when(jpaRepository.findByType("cliente")).thenReturn(Optional.of(jpaEntity));
        when(mapper.toDomain(jpaEntity)).thenReturn(profile);

        Optional<Profile> result = profileRepository.findByType("cliente");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(profile);
    }
}

package br.com.fiap.cheffy.infrastructure.persistence.profile.adapter;

import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.infrastructure.persistence.profile.entity.ProfileJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.profile.mapper.ProfilePersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.profile.repository.ProfileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepository {

    private final ProfileJpaRepository jpaRepository;
    private final ProfilePersistenceMapper mapper;

    public Optional<Profile> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Profile> findByType(String type) {
        return jpaRepository.findByType(type)
                .map(mapper::toDomain);
    }

    @Override
    public Long save(Profile profileDomain) {
        ProfileJpaEntity profileJpa = mapper.toJpaReference(profileDomain);
        return jpaRepository.save(profileJpa).getId();
    }


}

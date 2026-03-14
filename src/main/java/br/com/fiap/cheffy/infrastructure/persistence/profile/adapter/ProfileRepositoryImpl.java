package br.com.fiap.cheffy.infrastructure.persistence.profile.adapter;

import br.com.fiap.cheffy.application.profile.dto.PageInputPort;
import br.com.fiap.cheffy.application.profile.dto.PageOutputPort;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.infrastructure.persistence.profile.entity.ProfileJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.profile.mapper.ProfilePersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.profile.repository.ProfileJpaRepository;
import br.com.fiap.cheffy.infrastructure.persistence.util.mapper.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryImpl implements ProfileRepository {

    private final ProfileJpaRepository profileJpaRepository;
    private final ProfilePersistenceMapper mapper;
    private final PageMapper pageMapper;

    public Optional<Profile> findById(Long id) {
        return profileJpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Profile> findByType(String type) {
        return profileJpaRepository.findByType(type)
                .map(mapper::toDomain);
    }

    @Override
    public Long save(Profile profileDomain) {
        ProfileJpaEntity profileJpa = mapper.toJpaReference(profileDomain);
        return profileJpaRepository.save(profileJpa).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public PageOutputPort<Profile> findAll(PageInputPort pageInput) {
        final Page<Profile> map = profileJpaRepository.findAll(pageMapper.toPageable(pageInput))
                .map(mapper::toDomain);
        return pageMapper.toPageOutputPort(map);
    }


}

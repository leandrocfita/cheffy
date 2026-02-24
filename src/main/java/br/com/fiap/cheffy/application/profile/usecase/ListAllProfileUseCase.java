package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.application.profile.mapper.ProfileQueryMapper;
import br.com.fiap.cheffy.domain.profile.port.input.ListAllProfilesInput;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public class ListAllProfileUseCase implements ListAllProfilesInput {

    private final ProfileRepository profileRepository;
    private final ProfileQueryMapper mapper;

    public ListAllProfileUseCase(ProfileRepository profileRepository, ProfileQueryMapper mapper) {
        this.profileRepository = profileRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfileQueryPort> execute(Pageable pageable) {
        return profileRepository.findAll(pageable)
                .map(mapper::toQuery);
    }
}

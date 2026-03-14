package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.dto.PageInputPort;
import br.com.fiap.cheffy.application.profile.dto.PageOutputPort;
import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import br.com.fiap.cheffy.application.profile.mapper.ProfileQueryMapper;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.port.input.ListAllProfilesInput;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
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
    public PageOutputPort<ProfileQueryPort> execute(PageInputPort input) {
        final PageOutputPort<Profile> page = profileRepository.findAll(input);

        return new PageOutputPort<>(
                page.getContent().stream().map(mapper::toQuery).toList(),
                page.getPage(),
                page.getSize(),
                page.getTotalElements()
        );
    }
}

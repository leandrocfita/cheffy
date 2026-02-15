package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputDto;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.port.input.IProfileCreateInput;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;

public class CreateProfileUseCase implements IProfileCreateInput {

    private final ProfileRepository profileRepository;

    public CreateProfileUseCase(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    public Long create(ProfileInputDto profileInformation) {

        String nameType = profileInformation.name();

        Profile profile = CreateProfileUseCase.createProfileDomain(null,nameType);

        return profileRepository.save(profile);
    }

    private static Profile createProfileDomain(Long id, String profileType){

        return Profile.create(id, profileType);
    }
}

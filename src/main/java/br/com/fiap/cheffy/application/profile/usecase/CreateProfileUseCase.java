package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileAlreadyExistException;
import br.com.fiap.cheffy.domain.profile.port.input.ProfileCreateInput;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

public class CreateProfileUseCase implements ProfileCreateInput {

    private final ProfileRepository profileRepository;

    public CreateProfileUseCase(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    public Long create(ProfileInputPort profileInformation) {

        String nameType = profileInformation.name();

        Profile profile = createProfileDomain(null,nameType);

        Profile profileFound = profileRepository.findByType(nameType).orElse(null);

        if (profileFound != null) {
            throw new ProfileAlreadyExistException(ExceptionsKeys.PROFILE_ALREADY_EXIST_EXCEPTION, nameType);
        }

        return profileRepository.save(profile);
    }

    private Profile createProfileDomain(Long id, String profileType){

        return Profile.create(id, profileType);
    }
}

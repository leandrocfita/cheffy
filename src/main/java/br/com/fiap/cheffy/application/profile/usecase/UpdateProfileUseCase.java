package br.com.fiap.cheffy.application.profile.usecase;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputPort;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.profile.port.input.ProfileUpdateInput;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateProfileUseCase implements ProfileUpdateInput {

    private final Logger logger = Logger.getLogger(UpdateProfileUseCase.class.getName());

    private final ProfileRepository profileRepository;

    public UpdateProfileUseCase(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    @Override
    public void updateById(Long id, ProfileInputPort profileInputPort){

        logger.info("Entered in UpdateProfileUseCase: Starting updating flow for profile with id: {" + id + "}");

        String newProfileType = profileInputPort.name();

        Profile profileFound = profileRepository.findById(id).orElse(null);

        if (profileFound == null) {
            logger.log(Level.WARNING, "Profile with id {0} not found, aborting the profile type update", id);
            throw  new ProfileNotFoundException(ExceptionsKeys.PROFILE_NOT_FOUND_EXCEPTION, id.toString());
        }

        profileFound.patch(newProfileType);

        profileRepository.save(profileFound);

        logger.info("End of UpdateProfileUseCase: Profile updated complete");


    }

    @Override
    public void updateByName(String nameType, ProfileInputPort profileInputPort){

        logger.info("Entered in UpdateProfileUseCase: Starting updating flow for profile with name: {" + nameType + "}");

        String newProfileType = profileInputPort.name();

        Profile profileFound = profileRepository.findByType(nameType).orElse(null);

        if (profileFound == null) {
            logger.log(Level.WARNING, "Profile with nameType {0} not found, aborting the profile type update", nameType);
            throw  new ProfileNotFoundException(ExceptionsKeys.PROFILE_NOT_FOUND_EXCEPTION, nameType);
        }

        profileFound.patch(newProfileType);

        profileRepository.save(profileFound);

        logger.info("End of UpdateProfileUseCase: Profile updated complete");

    }
}

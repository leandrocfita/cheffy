package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.profile.mapper.ProfileQueryMapper;
import br.com.fiap.cheffy.application.profile.usecase.CreateProfileUseCase;
import br.com.fiap.cheffy.application.profile.usecase.ListAllProfileUseCase;
import br.com.fiap.cheffy.application.profile.usecase.UpdateProfileUseCase;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfileUseCaseConfig {


    @Bean
    CreateProfileUseCase createProfileUseCase(ProfileRepository profileRepository) {

        return new CreateProfileUseCase(profileRepository);
    }

    @Bean
    public ListAllProfileUseCase listAllProfilesUseCase(
            ProfileRepository profileRepository,
            ProfileQueryMapper mapper) {
        return new ListAllProfileUseCase(profileRepository, mapper);
    }

    @Bean
    UpdateProfileUseCase updateProfileUseCase(ProfileRepository profileRepository) {
        return new UpdateProfileUseCase(profileRepository);
    }
}

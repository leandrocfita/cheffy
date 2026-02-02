package br.com.fiap.cheffy.infrastructure.config;

import br.com.fiap.cheffy.application.user.usecase.CreateUserUseCase;
import br.com.fiap.cheffy.domain.profile.port.input.PasswordEncoderPort;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            PasswordEncoderPort passwordEncoderPort
    ) {
        return new CreateUserUseCase(
                userRepository,
                profileRepository,
                passwordEncoderPort);
    }
}

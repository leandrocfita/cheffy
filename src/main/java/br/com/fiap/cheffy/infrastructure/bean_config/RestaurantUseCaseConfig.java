package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.restaurant.usecase.DeactivateRestaurantUseCase;
import br.com.fiap.cheffy.application.restaurant.usecase.ReactivateRestaurantUseCase;
import br.com.fiap.cheffy.application.restaurant.usecase.RegisterRestaurantUseCase;
import br.com.fiap.cheffy.application.user.service.UserServiceHelper;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantUseCaseConfig {

    @Bean
    public RegisterRestaurantUseCase registerRestarantUseCase(
            UserServiceHelper userServiceHelper,
            RestaurantRepository restaurantRepository,
            ProfileRepository profileRepository
    ) {
        return new RegisterRestaurantUseCase(
                userServiceHelper,
                restaurantRepository,
                profileRepository
        );

    }

    @Bean
    public DeactivateRestaurantUseCase deactivateRestaurantUseCase(
            RestaurantRepository restaurantRepository
    ) {
        return new DeactivateRestaurantUseCase(
                restaurantRepository
        );
    }

    @Bean
    public ReactivateRestaurantUseCase reactivateRestaurantUseCase(
            RestaurantRepository restaurantRepository
    ) {
        return new ReactivateRestaurantUseCase(
                restaurantRepository
        );
    }
}

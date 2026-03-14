package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
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
    public RestaurantServiceHelper restaurantServiceHelper(
            RestaurantRepository restaurantRepository
    ) {
        return new RestaurantServiceHelper(restaurantRepository);
    }

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
            RestaurantServiceHelper restaurantServiceHelper
    ) {
        return new DeactivateRestaurantUseCase(
                restaurantServiceHelper
        );
    }

    @Bean
    public ReactivateRestaurantUseCase reactivateRestaurantUseCase(
            RestaurantServiceHelper restaurantServiceHelper
    ) {
        return new ReactivateRestaurantUseCase(
                restaurantServiceHelper
        );
    }
}

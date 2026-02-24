package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.restaurant.usecase.RegisterRestaurantUseCase;
import br.com.fiap.cheffy.application.user.service.UserServiceHelper;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseConfigTest {

    @Mock
    private UserServiceHelper userServiceHelper;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private ProfileRepository profileRepository;

    @Test
    void registerRestarantUseCaseCreatesBean() {
        RestaurantUseCaseConfig config = new RestaurantUseCaseConfig();

        RegisterRestaurantUseCase useCase = config.registerRestarantUseCase(
                userServiceHelper,
                restaurantRepository,
                profileRepository
        );

        assertThat(useCase).isNotNull();
    }
}

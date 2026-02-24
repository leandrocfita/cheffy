package br.com.fiap.cheffy.application.restaurant.usecase;

import br.com.fiap.cheffy.application.restaurant.dto.RestaurantCommandPort;
import br.com.fiap.cheffy.application.user.service.UserServiceHelper;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.port.input.RegisterRestaurantInput;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.shared.exception.BusinessException;
import br.com.fiap.cheffy.shared.exception.InvalidDataException;
import br.com.fiap.cheffy.shared.exception.RegisterFailedException;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;

public class RegisterRestarantUseCase implements RegisterRestaurantInput {

    private final UserServiceHelper userServiceHelper;
    private final RestaurantRepository restaurantRepository;
    private final ProfileRepository profileRepository;

    public RegisterRestarantUseCase(
            UserServiceHelper userServiceHelper,
            RestaurantRepository restaurantRepository,
            ProfileRepository profileRepository
    ) {
        this.userServiceHelper = userServiceHelper;
        this.restaurantRepository = restaurantRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public String execute(RestaurantCommandPort restaurant, UUID userId) {

        throwExceptionCaseRestaurantAlreadyRegistered(restaurant);

        User user = userServiceHelper.getUserOrFail(userId);

        Restaurant restaurantDomain = createRestaurantDomain(restaurant, user);
        Address address = createAddressDomain(restaurant);
        restaurantDomain.addAddress(address);

        String savedId = restaurantRepository.save(restaurantDomain).getId().toString();

        if(hasntOwnerProfile(user)){
            assignOwnerProfileAndSave(user);
        }

        return savedId;
    }

    private void throwExceptionCaseRestaurantAlreadyRegistered(RestaurantCommandPort restaurant) {
        boolean exists = restaurantRepository.existsByCnpj(restaurant.cnpj())
                || restaurantRepository.existsByName(restaurant.name());
        if (exists) {
            throw new RegisterFailedException(RESTAURANT_ALREADY_EXIST);
        }
    }

    private void assignOwnerProfileAndSave(User user) {
        user.addProfile(getOwnerProfileOrFail());
        userServiceHelper.saveUser(user);
    }

    private static boolean hasntOwnerProfile(User user) {
        return !user.getProfiles()
                .stream()
                .anyMatch(profile -> profile.getType().equals(ProfileType.OWNER.getType()));
    }

    private Profile getOwnerProfileOrFail() {
        String owner = ProfileType.OWNER.getType();

        return profileRepository.findByType(owner)
                .orElseThrow(() -> new ProfileNotFoundException(PROFILE_NOT_FOUND_EXCEPTION,
                        owner));
    }

    private Address createAddressDomain(RestaurantCommandPort command) {
        return Address.create(
                command.address().streetName(),
                command.address().number(),
                command.address().city(),
                command.address().postalCode(),
                command.address().neighborhood(),
                command.address().stateProvince(),
                command.address().addressLine(),
                true
        );
    }

    private Restaurant createRestaurantDomain(RestaurantCommandPort restaurant, User user) {
        ZoneId zoneId = extractZoneId(restaurant);
        return restaurant.open24hours() ? Restaurant.create24h(
                restaurant.name(),
                restaurant.cnpj(),
                restaurant.culinary(),
                zoneId,
                user
        ) : Restaurant.createWithWorkingHours(
                restaurant.name(),
                restaurant.cnpj(),
                restaurant.culinary(),
                zoneId,
                restaurant.openingTime(),
                restaurant.closingTime(),
                user
        );
    }

    private static ZoneId extractZoneId(RestaurantCommandPort restaurant) {
        try {
            return ZoneId.of(restaurant.zoneId());
        } catch (DateTimeException ex) {
            throw new InvalidDataException(ZONE_ID_DO_NOT_EXIST);
        }
    }
}

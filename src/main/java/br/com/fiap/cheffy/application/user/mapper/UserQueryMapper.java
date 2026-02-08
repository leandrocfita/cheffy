package br.com.fiap.cheffy.application.user.mapper;

import br.com.fiap.cheffy.application.user.dto.AddressQueryPort;
import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserQueryMapper {

    public UserQueryPort toQuery(User user) {
        return new UserQueryPort(
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getPassword(),
                toProfileTypes(user),
                toAddressQueries(user)
        );
    }

    private Set<ProfileType> toProfileTypes(User user) {
        return user.getProfiles().stream()
                .map(profile -> ProfileType.valueOf(profile.getType()))
                .collect(Collectors.toSet());
    }

    private Set<AddressQueryPort> toAddressQueries(User user) {
        return user.getAddresses().stream()
                .map(this::toAddressQuery)
                .collect(Collectors.toSet());
    }

    private AddressQueryPort toAddressQuery(Address address) {
        return new AddressQueryPort(
                address.getId(),
                address.getStreetName(),
                address.getNumber(),
                address.getCity(),
                address.getPostalCode(),
                address.getNeighborhood(),
                address.getStateProvince(),
                address.getAddressLine(),
                address.isMain()
        );
    }
}

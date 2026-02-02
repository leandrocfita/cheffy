package br.com.fiap.cheffy.infrastructure.persistence.user.mapper;

import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.infrastructure.persistence.profile.mapper.ProfilePersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.user.entity.AddressJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.user.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserPersistenceMapper {

    private final ProfilePersistenceMapper profileMapper;

    public UserPersistenceMapper(ProfilePersistenceMapper profileMapper) {
        this.profileMapper = profileMapper;
    }

    public UserJpaEntity toJpa(User user) {
        UserJpaEntity jpa = new UserJpaEntity();

        jpa.setId(user.getId());
        jpa.setName(user.getName());
        jpa.setEmail(user.getEmail());
        jpa.setLogin(user.getLogin());
        jpa.setPassword(user.getPassword());

        jpa.setProfiles(
                user.getProfiles().stream()
                        .map(profileMapper::toJpaReference)
                        .collect(Collectors.toSet())
        );

        jpa.setAddresses(
                user.getAddresses().stream()
                        .map(address -> toJpa(address, jpa))
                        .collect(Collectors.toSet())
        );

        return jpa;
    }

    public User toDomain(UserJpaEntity jpa) {
        User user = new User(
                jpa.getId(),
                jpa.getName(),
                jpa.getEmail(),
                jpa.getLogin(),
                jpa.getPassword()
        );

        jpa.getProfiles().forEach(p ->
                user.addProfile(profileMapper.toDomain(p))
        );

        jpa.getAddresses().forEach(a ->
                user.addAddress(toDomain(a))
        );

        return user;
    }

    private AddressJpaEntity toJpa(Address address, UserJpaEntity userJpa) {
        AddressJpaEntity jpa = new AddressJpaEntity();

        jpa.setId(address.getId());
        jpa.setStreetName(address.getStreetName());
        jpa.setNumber(address.getNumber());
        jpa.setCity(address.getCity());
        jpa.setPostalCode(address.getPostalCode());
        jpa.setNeighborhood(address.getNeighborhood());
        jpa.setStateProvince(address.getStateProvince());
        jpa.setAddressLine(address.getAddressLine());
        jpa.setMain(address.isMain());
        jpa.setUser(userJpa);

        return jpa;
    }

    private Address toDomain(AddressJpaEntity jpa) {
        return new Address(
                jpa.getId(),
                jpa.getStreetName(),
                jpa.getNumber(),
                jpa.getCity(),
                jpa.getPostalCode(),
                jpa.getNeighborhood(),
                jpa.getStateProvince(),
                jpa.getAddressLine(),
                jpa.getMain()
        );
    }
}

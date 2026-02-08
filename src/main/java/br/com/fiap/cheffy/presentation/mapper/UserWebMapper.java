package br.com.fiap.cheffy.presentation.mapper;

import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;
import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
import br.com.fiap.cheffy.presentation.dto.AddressCreateDTO;
import br.com.fiap.cheffy.presentation.dto.UserCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class UserWebMapper {

    public UserCommandPort toCommand(UserCreateDTO request) {
        return new UserCommandPort(
                request.name(),
                request.email(),
                request.login(),
                request.password(),
                request.profileType(),
                new AddressCommandPort(
                        request.address().streetName(),
                        request.address().number(),
                        request.address().city(),
                        request.address().postalCode(),
                        request.address().neighborhood(),
                        request.address().stateProvince(),
                        request.address().addressLine(),
                        request.address().main())
        );
    }

    public AddressCommandPort toCommand(AddressCreateDTO request) {
        return new AddressCommandPort(
                request.streetName(),
                request.number(),
                request.city(),
                request.postalCode(),
                request.neighborhood(),
                request.stateProvince(),
                request.addressLine(),
                request.main()
        );
    }

}

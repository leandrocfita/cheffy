package br.com.fiap.cheffy.presentation.mapper;

import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;
import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
import br.com.fiap.cheffy.presentation.dto.AddressCreateDTO;
import br.com.fiap.cheffy.presentation.dto.UserCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class UserWebMapper {

    public UserCommandPort toCommand(UserCreateDTO userCreateDTO) {
        AddressCreateDTO address = userCreateDTO.address();
        
        return new UserCommandPort(
                userCreateDTO.name(),
                userCreateDTO.email(),
                userCreateDTO.login(),
                userCreateDTO.password(),
                new AddressCommandPort(
                        address.streetName(),
                        address.number(),
                        address.city(),
                        address.postalCode(),
                        address.neighborhood(),
                        address.stateProvince(),
                        address.addressLine(),
                        address.main())
        );
    }

}

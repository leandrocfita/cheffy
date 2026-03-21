package br.com.fiap.cheffy.application.user.dto;

import br.com.fiap.cheffy.domain.profile.ProfileType;

public record UserCommandPort(

        String name,
        String email,
        String login,
        String password,
        ProfileType profileType,
        AddressCommandPort address
) {
}


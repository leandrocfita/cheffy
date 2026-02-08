package br.com.fiap.cheffy.application.user.dto;

import br.com.fiap.cheffy.domain.profile.ProfileType;

import java.util.Set;

public record UserQueryPort(
        String name,
        String email,
        String login,
        String password,
        Set<ProfileType> profileType,
        Set<AddressQueryPort> addresses
){}

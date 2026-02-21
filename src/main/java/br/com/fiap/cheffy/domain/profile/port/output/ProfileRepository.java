package br.com.fiap.cheffy.domain.profile.port.output;

import br.com.fiap.cheffy.domain.profile.entity.Profile;

import java.util.Optional;

public interface ProfileRepository {

    Optional<Profile> findById(Long id);
    Optional<Profile> findByType(String type);
    Long save(Profile profile);
}

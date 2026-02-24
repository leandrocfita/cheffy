package br.com.fiap.cheffy.domain.profile.port.output;

import br.com.fiap.cheffy.domain.profile.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProfileRepository {

    Optional<Profile> findById(Long id);

    Optional<Profile> findByType(String type);

    Long save(Profile profile);

    Page<Profile> findAll(Pageable pageable);
}

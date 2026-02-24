package br.com.fiap.cheffy.domain.profile.port.input;

import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListAllProfilesInput {

    Page<ProfileQueryPort> execute(Pageable pageable);
}

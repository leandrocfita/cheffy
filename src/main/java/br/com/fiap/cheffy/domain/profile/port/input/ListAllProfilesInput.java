package br.com.fiap.cheffy.domain.profile.port.input;

import br.com.fiap.cheffy.application.profile.dto.PageInputPort;
import br.com.fiap.cheffy.application.profile.dto.PageOutputPort;
import br.com.fiap.cheffy.application.profile.dto.ProfileQueryPort;

public interface ListAllProfilesInput {

    PageOutputPort<ProfileQueryPort> execute(PageInputPort inputPort);
}

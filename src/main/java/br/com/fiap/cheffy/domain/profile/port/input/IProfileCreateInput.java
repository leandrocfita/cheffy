package br.com.fiap.cheffy.domain.profile.port.input;

import br.com.fiap.cheffy.application.profile.dto.ProfileInputDto;

public interface IProfileCreateInput {

    Long create(ProfileInputDto profileInformation);
}

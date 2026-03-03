package br.com.fiap.cheffy.domain.user.port.input;

import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindUserByNameInput {
    Page<UserQueryPort> execute(String name, Pageable pageable);
}
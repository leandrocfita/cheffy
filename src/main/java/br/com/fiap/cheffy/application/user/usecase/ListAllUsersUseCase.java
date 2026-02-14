package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
import br.com.fiap.cheffy.domain.user.port.input.ListAllUsersInput;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListAllUsersUseCase implements ListAllUsersInput {

    private final UserRepository userRepository;
    private final UserQueryMapper mapper;

    public ListAllUsersUseCase(UserRepository userRepository, UserQueryMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<UserQueryPort> execute(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(mapper::toQuery);
    }
}
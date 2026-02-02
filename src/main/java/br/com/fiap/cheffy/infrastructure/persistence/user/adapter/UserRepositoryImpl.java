package br.com.fiap.cheffy.infrastructure.persistence.user.adapter;

import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import br.com.fiap.cheffy.infrastructure.persistence.user.mapper.UserPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper mapper;

    @Override
    public UUID save(User user) {
        var jpaEntity = mapper.toJpa(user);

        var saved = userJpaRepository.save(jpaEntity);

        return saved.getId();
    }

    @Override
    public boolean existsByEmailOrLogin(String email, String login) {
        return userJpaRepository.existsByEmailOrLogin(email, login);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userJpaRepository.findByLogin(login)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(mapper::toDomain);
    }
}

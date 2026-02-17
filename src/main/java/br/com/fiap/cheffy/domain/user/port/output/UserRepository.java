package br.com.fiap.cheffy.domain.user.port.output;

import br.com.fiap.cheffy.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    boolean existsByEmailOrLogin(String email, String login);

    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    Page<User> findAll(Pageable pageable);
}

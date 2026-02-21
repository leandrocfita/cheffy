package br.com.fiap.cheffy.infrastructure.persistence.user.adapter;

import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.infrastructure.persistence.user.entity.UserJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.user.mapper.UserPersistenceMapper;
import br.com.fiap.cheffy.infrastructure.persistence.user.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private UserJpaRepository jpaRepository;

    @Mock
    private UserPersistenceMapper mapper;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    @Test
    void saveUser() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", "pass");
        UserJpaEntity jpaEntity = new UserJpaEntity();
        when(mapper.toJpa(user)).thenReturn(jpaEntity);
        when(jpaRepository.save(jpaEntity)).thenReturn(jpaEntity);
        when(mapper.toDomain(jpaEntity)).thenReturn(user);

        User result = userRepository.save(user);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void existsByEmailOrLogin() {
        when(jpaRepository.existsByEmailOrLogin("email@test.com", "login")).thenReturn(true);

        boolean exists = userRepository.existsByEmailOrLogin("email@test.com", "login");

        assertThat(exists).isTrue();
    }

    @Test
    void findByLogin() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", "pass");
        UserJpaEntity jpaEntity = new UserJpaEntity();
        when(jpaRepository.findByLogin("login")).thenReturn(Optional.of(jpaEntity));
        when(mapper.toDomain(jpaEntity)).thenReturn(user);

        Optional<User> result = userRepository.findByLogin("login");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    void findByEmail() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", "pass");
        UserJpaEntity jpaEntity = new UserJpaEntity();
        when(jpaRepository.findByEmail("email@test.com")).thenReturn(Optional.of(jpaEntity));
        when(mapper.toDomain(jpaEntity)).thenReturn(user);

        Optional<User> result = userRepository.findByEmail("email@test.com");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    void findById() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Name", "email@test.com", "login", "pass");
        UserJpaEntity jpaEntity = new UserJpaEntity();
        when(jpaRepository.findById(id)).thenReturn(Optional.of(jpaEntity));
        when(mapper.toDomain(jpaEntity)).thenReturn(user);

        Optional<User> result = userRepository.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }
}

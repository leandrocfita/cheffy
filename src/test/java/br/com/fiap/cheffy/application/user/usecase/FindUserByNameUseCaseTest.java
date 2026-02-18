package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByNameUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserQueryMapper mapper;

    private FindUserByNameUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindUserByNameUseCase(userRepository, mapper);
    }

    @Test
    void shouldFindUsersByNameSuccessfully() {

        String name = "João";

        User firstUser = createUser("João Silva", "joao@email.com");
        User secondUser = createUser("João Pedro", "joaopedro@email.com");

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(
                List.of(firstUser, secondUser),
                pageable,
                2
        );

        UserQueryPort mappedFirst = mock(UserQueryPort.class);
        UserQueryPort mappedSecond = mock(UserQueryPort.class);

        when(userRepository.findByName(name, pageable)).thenReturn(userPage);
        when(mapper.toQuery(firstUser)).thenReturn(mappedFirst);
        when(mapper.toQuery(secondUser)).thenReturn(mappedSecond);

        Page<UserQueryPort> users = useCase.execute(name, pageable);

        assertNotNull(users);
        assertEquals(2, users.getTotalElements());
        assertEquals(2, users.getContent().size());

        verify(userRepository, times(1)).findByName(name, pageable);
        verify(mapper, times(1)).toQuery(firstUser);
        verify(mapper, times(1)).toQuery(secondUser);
    }

    @Test
    void shouldReturnEmptyPageWhenNoUsersFound() {

        String name = "Luan";

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(userRepository.findByName(name, pageable)).thenReturn(emptyPage);

        Page<UserQueryPort> users = useCase.execute(name, pageable);

        assertNotNull(users);
        assertEquals(0, users.getTotalElements());
        assertTrue(users.getContent().isEmpty());

        verify(userRepository, times(1)).findByName(name, pageable);
        verify(mapper, never()).toQuery(any());
    }

    @Test
    void shouldHandlePaginationCorrectly() {
        String name = "Carlos";
        User user = createUser("Carlos Oliveira", "carlos@email.com");

        Pageable pageable = PageRequest.of(1, 5);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 6);

        UserQueryPort mapped = mock(UserQueryPort.class);

        when(userRepository.findByName(name, pageable)).thenReturn(userPage);
        when(mapper.toQuery(user)).thenReturn(mapped);

        Page<UserQueryPort> users = useCase.execute(name, pageable);

        assertEquals(6, users.getTotalElements());
        assertEquals(2, users.getTotalPages());
        assertEquals(1, users.getNumber());
    }

    private User createUser(String name, String email) {
        Profile profile = new Profile(1L, ProfileType.CLIENT.getType());

        User user = User.create(
                name,
                email,
                name.toLowerCase().replace(" ", "."),
                "Pass@123456",
                profile
        );

        Address address = Address.create(
                "Rua Teste",
                100,
                "São Paulo",
                "01000000",
                "Centro",
                "SP",
                "Apto 10",
                true
        );

        user.addAddress(address);

        return user;
    }
}
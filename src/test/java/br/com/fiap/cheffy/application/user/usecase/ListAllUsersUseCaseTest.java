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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListAllUsersUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserQueryMapper mapper;

    private ListAllUsersUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListAllUsersUseCase(userRepository, mapper);
    }

    @Test
    void shouldListUsersSuccessfully() {

        User firstUser = createUser("João Silva", "joao.silva@email.com");
        User secondUser = createUser("Maria Souza", "maria.souza@email.com");

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(Arrays.asList(firstUser, secondUser), pageable, 2);

        UserQueryPort mappedFirstUser = mock(UserQueryPort.class);
        UserQueryPort mappedSecondUser = mock(UserQueryPort.class);

        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(mapper.toQuery(firstUser)).thenReturn(mappedFirstUser);
        when(mapper.toQuery(secondUser)).thenReturn(mappedSecondUser);


        Page<UserQueryPort> usersPageResult = useCase.execute(pageable);

        assertNotNull(usersPageResult);
        assertEquals(2, usersPageResult.getTotalElements());
        assertEquals(2, usersPageResult.getContent().size());
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(userRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<UserQueryPort> usersPageResult = useCase.execute(pageable);

        assertNotNull(usersPageResult);
        assertEquals(0, usersPageResult.getTotalElements());
        assertTrue(usersPageResult.getContent().isEmpty());
    }

    @Test
    void shouldHandlePagination() {
        User user = createUser("Carlos Oliveira", "carlos.oliveira@email.com");

        Pageable pageable = PageRequest.of(1, 5);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 10);

        UserQueryPort query = mock(UserQueryPort.class);
        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(mapper.toQuery(user)).thenReturn(query);


        Page<UserQueryPort> result = useCase.execute(pageable);


        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals(1, result.getNumber());
    }

    private User createUser(String name, String email) {
        Profile profile = Profile.create(1L, ProfileType.CLIENT.getType());
        User user = User.create(
                name,
                email,
                name.toLowerCase().replace(" ", "."),
                "Pass@1234567890",
                profile
        );

        Address address = Address.create(
                "Avenida Paulista",
                1000,
                "São Paulo",
                "01311000",
                "Bela Vista",
                "SP",
                "Apartamento 101",
                true
        );

        user.addAddress(address);

        return user;
    }
}
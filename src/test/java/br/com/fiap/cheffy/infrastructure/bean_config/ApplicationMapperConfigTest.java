package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationMapperConfigTest {

    @Test
    void createMappers() {
        ApplicationMapperConfig config = new ApplicationMapperConfig();

        UserQueryMapper userQueryMapper = config.userQueryMapper();
        assertThat(userQueryMapper).isNotNull();
    }
}

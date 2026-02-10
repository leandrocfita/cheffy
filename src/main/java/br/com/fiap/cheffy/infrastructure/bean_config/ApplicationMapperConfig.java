package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationMapperConfig {

    @Bean
    public UserQueryMapper userQueryMapper() {
        return new UserQueryMapper();
    }
}

package br.com.fiap.cheffy.domain.profile.exception;

import org.junit.jupiter.api.Test;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.PROFILE_NOT_FOUND_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;

class ProfileNotFoundExceptionTest {

    @Test
    void profileNotFoundExceptionWithType() {
        ProfileNotFoundException ex = new ProfileNotFoundException(PROFILE_NOT_FOUND_EXCEPTION, "CLIENT");
        assertThat(ex.getMessage()).contains("PROFILE_NOT_FOUND_EXCEPTION");
        assertThat(ex.getType()).isEqualTo("CLIENT");
    }
}

package br.com.fiap.cheffy.domain.profile.entity;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileTest {

    @Test
    void createProfile() {
        Profile profile = Profile.create(1L, ProfileType.CLIENT.getType());
        
        assertThat(profile.getId()).isEqualTo(1L);
        assertThat(profile.getType()).isEqualTo(ProfileType.CLIENT.getType());
    }
}

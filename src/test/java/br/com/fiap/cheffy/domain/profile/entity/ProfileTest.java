package br.com.fiap.cheffy.domain.profile.entity;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {

    @Test
    void createProfile() {
        Profile profile = Profile.create(1L, ProfileType.CLIENT.getType());
        
        assertThat(profile.getId()).isEqualTo(1L);
        assertThat(profile.getType()).isEqualTo(ProfileType.CLIENT.getType());
    }

    @Test
    void createProfileWithInvalidType() {
        assertThatThrownBy(() -> Profile.create(1L, ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Profile type cannot be null or empty");

        assertThatThrownBy(() -> Profile.create(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Profile type cannot be null or empty");
    }

    @Test
    void patchProfile(){
        Profile profile = Profile.create(1L, ProfileType.CLIENT.getType());
        profile.patch(ProfileType.OWNER.getType());

        assertThat(profile.getType()).isEqualTo(ProfileType.OWNER.getType());
    }

    @Test
    void patchProfileWithInvalidType(){
        Profile profile = Profile.create(1L, ProfileType.CLIENT.getType());

        assertThatThrownBy(() -> profile.patch(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Profile type cannot be null or empty");

        assertThatThrownBy(() -> profile.patch(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Profile type cannot be null or empty");
    }
}

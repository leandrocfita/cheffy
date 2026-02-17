package br.com.fiap.cheffy.infrastructure.security.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SpringAuthenticatedUserTest {

    @Test
    void shouldCreateSpringAuthenticatedUser() {
        UUID id = UUID.randomUUID();
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        
        SpringAuthenticatedUser user = new SpringAuthenticatedUser(id, "username", "password", authorities);
        
        assertEquals(id, user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals(authorities, user.getAuthorities());
    }

    @Test
    void shouldReturnTrueForAccountNonExpired() {
        SpringAuthenticatedUser user = new SpringAuthenticatedUser(
            UUID.randomUUID(), "user", "pass", Set.of()
        );
        
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void shouldReturnTrueForAccountNonLocked() {
        SpringAuthenticatedUser user = new SpringAuthenticatedUser(
            UUID.randomUUID(), "user", "pass", Set.of()
        );
        
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void shouldReturnTrueForCredentialsNonExpired() {
        SpringAuthenticatedUser user = new SpringAuthenticatedUser(
            UUID.randomUUID(), "user", "pass", Set.of()
        );
        
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void shouldReturnTrueForEnabled() {
        SpringAuthenticatedUser user = new SpringAuthenticatedUser(
            UUID.randomUUID(), "user", "pass", Set.of()
        );
        
        assertTrue(user.isEnabled());
    }

    @Test
    void shouldHandleMultipleAuthorities() {
        Set<GrantedAuthority> authorities = Set.of(
            new SimpleGrantedAuthority("ROLE_USER"),
            new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        
        SpringAuthenticatedUser user = new SpringAuthenticatedUser(
            UUID.randomUUID(), "admin", "pass", authorities
        );
        
        assertEquals(2, user.getAuthorities().size());
    }

    @Test
    void shouldUseGettersFromLombok() {
        UUID id = UUID.randomUUID();
        SpringAuthenticatedUser user = new SpringAuthenticatedUser(id, "user", "pass", Set.of());
        
        assertNotNull(user.getId());
        assertNotNull(user.getUsername());
        assertNotNull(user.getPassword());
        assertNotNull(user.getAuthorities());
    }

    @Test
    void equalsAndHashCodeWork() {
        UUID id = UUID.randomUUID();
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        
        SpringAuthenticatedUser user1 = new SpringAuthenticatedUser(id, "user", "pass", authorities);
        SpringAuthenticatedUser user2 = new SpringAuthenticatedUser(id, "user", "pass", authorities);
        SpringAuthenticatedUser user3 = new SpringAuthenticatedUser(UUID.randomUUID(), "other", "pass", authorities);
        
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void toStringWorks() {
        UUID id = UUID.randomUUID();
        SpringAuthenticatedUser user = new SpringAuthenticatedUser(id, "user", "pass", Set.of());
        
        String toString = user.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("SpringAuthenticatedUser"));
    }

    @Test
    void equalsWithNull() {
        UUID id = UUID.randomUUID();
        SpringAuthenticatedUser user = new SpringAuthenticatedUser(id, "user", "pass", Set.of());
        
        assertNotEquals(user, null);
    }

    @Test
    void equalsWithDifferentClass() {
        UUID id = UUID.randomUUID();
        SpringAuthenticatedUser user = new SpringAuthenticatedUser(id, "user", "pass", Set.of());
        
        assertNotEquals(user, "string");
    }

    @Test
    void equalsWithSameObject() {
        UUID id = UUID.randomUUID();
        SpringAuthenticatedUser user = new SpringAuthenticatedUser(id, "user", "pass", Set.of());
        
        assertEquals(user, user);
    }

    @Test
    void equalsWithDifferentId() {
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        SpringAuthenticatedUser user1 = new SpringAuthenticatedUser(UUID.randomUUID(), "user", "pass", authorities);
        SpringAuthenticatedUser user2 = new SpringAuthenticatedUser(UUID.randomUUID(), "user", "pass", authorities);
        
        assertNotEquals(user1, user2);
    }

    @Test
    void equalsWithDifferentUsername() {
        UUID id = UUID.randomUUID();
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        SpringAuthenticatedUser user1 = new SpringAuthenticatedUser(id, "user1", "pass", authorities);
        SpringAuthenticatedUser user2 = new SpringAuthenticatedUser(id, "user2", "pass", authorities);
        
        assertNotEquals(user1, user2);
    }

    @Test
    void equalsWithDifferentPassword() {
        UUID id = UUID.randomUUID();
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        SpringAuthenticatedUser user1 = new SpringAuthenticatedUser(id, "user", "pass1", authorities);
        SpringAuthenticatedUser user2 = new SpringAuthenticatedUser(id, "user", "pass2", authorities);
        
        assertNotEquals(user1, user2);
    }

    @Test
    void equalsWithDifferentAuthorities() {
        UUID id = UUID.randomUUID();
        SpringAuthenticatedUser user1 = new SpringAuthenticatedUser(id, "user", "pass", Set.of(new SimpleGrantedAuthority("ROLE_USER")));
        SpringAuthenticatedUser user2 = new SpringAuthenticatedUser(id, "user", "pass", Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        
        assertNotEquals(user1, user2);
    }

    @Test
    void hashCodeConsistency() {
        UUID id = UUID.randomUUID();
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        SpringAuthenticatedUser user = new SpringAuthenticatedUser(id, "user", "pass", authorities);
        
        int hash1 = user.hashCode();
        int hash2 = user.hashCode();
        
        assertEquals(hash1, hash2);
    }
}

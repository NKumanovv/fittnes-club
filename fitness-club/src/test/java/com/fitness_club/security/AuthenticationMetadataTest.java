package com.fitness_club.security;

import com.fitness_club.user.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationMetadataTest {

    @Test
    void testConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        AuthenticationMetadata metadata = new AuthenticationMetadata(id, "testuser", "password", UserRole.USER, true);

        assertEquals(id, metadata.getUserId());
        assertEquals("testuser", metadata.getUsername());
        assertEquals("password", metadata.getPassword());
        assertEquals(UserRole.USER, metadata.getRole());
        assertTrue(metadata.isActive());
    }

    @Test
    void getAuthorities_ReturnsCorrectRoleAuthority() {
        AuthenticationMetadata metadata = new AuthenticationMetadata(UUID.randomUUID(), "admin", "pass", UserRole.ADMIN, true);

        Collection<? extends GrantedAuthority> authorities = metadata.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void isAccountStatusMethods_ReturnIsActiveValue() {
        AuthenticationMetadata activeUser = new AuthenticationMetadata(UUID.randomUUID(), "user", "pass", UserRole.USER, true);

        assertTrue(activeUser.isAccountNonExpired());
        assertTrue(activeUser.isAccountNonLocked());
        assertTrue(activeUser.isCredentialsNonExpired());
        assertTrue(activeUser.isEnabled());

        AuthenticationMetadata inactiveUser = new AuthenticationMetadata(UUID.randomUUID(), "user", "pass", UserRole.USER, false);

        assertFalse(inactiveUser.isAccountNonExpired());
        assertFalse(inactiveUser.isAccountNonLocked());
        assertFalse(inactiveUser.isCredentialsNonExpired());
        assertFalse(inactiveUser.isEnabled());
    }
}
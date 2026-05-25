package com.example.carcare.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    // Clave Base64 de 256 bits (mínimo para HS256)
    private static final String SECRET =
            "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long EXPIRATION_MS = 3_600_000L; // 1 hora

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expirationMs", EXPIRATION_MS);
    }

    private UserDetails usuario(String email) {
        return new User(email, "hash", List.of());
    }

    // ─── generación ───────────────────────────────────────────────────────────

    @Test
    void generateToken_devuelveStringNoVacio() {
        String token = jwtService.generateToken(usuario("user@test.com"));

        assertThat(token).isNotBlank();
    }

    @Test
    void generateToken_tokenContieneEmailComoSubject() {
        String token = jwtService.generateToken(usuario("user@test.com"));

        assertThat(jwtService.extractEmail(token)).isEqualTo("user@test.com");
    }

    // ─── validación ───────────────────────────────────────────────────────────

    @Test
    void isTokenValid_devuelveTrue_paraMismoUsuario() {
        UserDetails user = usuario("user@test.com");
        String token = jwtService.generateToken(user);

        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }

    @Test
    void isTokenValid_devuelveFalse_paraDistintoUsuario() {
        String token = jwtService.generateToken(usuario("alice@test.com"));

        assertThat(jwtService.isTokenValid(token, usuario("bob@test.com"))).isFalse();
    }
}

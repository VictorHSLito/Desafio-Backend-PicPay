package com.victor.picpay.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.victor.picpay.dtos.requests.LoginDTO;
import com.victor.picpay.entities.User;
import com.victor.picpay.repositories.UserRepository;
import com.victor.picpay.security.jwt.JwtTokenService;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private LoginService loginService;

    @Spy
    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenService, "ISSUER", "test-issuer");
        ReflectionTestUtils.setField(jwtTokenService, "KEY", "my-private-key-test");
        jwtTokenService.init();
    }

    @Test
    @DisplayName("Should Generate Token Jwt Corectly When Credentials Matches")
    void shouldGenerateTokenJwtCorrectlyWhenCredentialsMatches() {
        LoginDTO dto = new LoginDTO(
            "user.test@gmail.com",
            "anyPassword#123"
        );

        User user = User.builder()
        .build();

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);

        String token = jwtTokenService.generateToken(dto);

        when(jwtTokenService.generateToken(dto)).thenReturn(token);

        var output = loginService.verifyUserLogin(dto);

        assertNotNull(output);
        assertEquals(token, output.token());
    }

    @Test
    @DisplayName("Should Return Null When Credentials Are Incorrect")
    void shouldReturnNullWhenCredentialsAreIncorrect() {
        LoginDTO dto = new LoginDTO(
            "user.test@gmail.com",
            "anyPassword123"
        );

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());

        var output = loginService.verifyUserLogin(dto);

        assertAll("Verify if Token is null",
            () -> assertNull(output),
            () -> verifyNoInteractions(passwordEncoder),
            () -> verify(jwtTokenService, times(0)).generateToken(any())
        );
    }   
}

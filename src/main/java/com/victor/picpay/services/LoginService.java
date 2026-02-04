package com.victor.picpay.services;

import com.victor.picpay.dtos.LoginDTO;
import com.victor.picpay.repositories.UserRepository;
import com.victor.picpay.security.jwt.JwtTokenService;
import com.victor.picpay.security.jwt.RecoveryJwtTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RecoveryJwtTokenDTO verifyUserLogin(LoginDTO loginDTO) {
        var user = userRepository.findByEmail(loginDTO.email());

        if (user.isPresent()) {
            String password = user.get().getPassword();

            if (passwordEncoder.matches(loginDTO.password(), password)) {
                String token = jwtTokenService.generateToken(loginDTO);

                return new RecoveryJwtTokenDTO(token);
            }
        }
        return null;
    }
}

package com.victor.picpay.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.victor.picpay.dtos.requests.LoginDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

// Reference: https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html
// Reference: https://medium.com/@felipeacelinoo/protegendo-sua-api-rest-com-spring-security-e-autenticando-usu%C3%A1rios-com-token-jwt-em-uma-aplica%C3%A7%C3%A3o-d70e5b0331f9

@Service
public class JwtTokenService {
    @Value("${jwt.issuer.value}")
    private String ISSUER;

    @Value("${jwt.secretkey.value}")
    private String KEY;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(KEY);
    }

    public String generateToken(LoginDTO loginDTO) {
        try {
            return JWT.create()
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusSeconds(3000L))
                    .withIssuer(ISSUER)
                    .withSubject(loginDTO.email())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new JWTCreationException("Error ao tentar criar o token", e);
        }
    }

    public String getSubjectFromToken(String token) {
        try {
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Erro ao identificar token", e);
        }
    }
}

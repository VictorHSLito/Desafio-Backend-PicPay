package com.victor.picpay.controllers;

import com.victor.picpay.dtos.LoginDTO;
import com.victor.picpay.security.jwt.RecoveryJwtTokenDTO;
import com.victor.picpay.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Reference: https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html#publish-authentication-manager-bean

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<RecoveryJwtTokenDTO> handleLogin(@RequestBody LoginDTO loginDTO) {
        RecoveryJwtTokenDTO token = loginService.verifyUserLogin(loginDTO);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}

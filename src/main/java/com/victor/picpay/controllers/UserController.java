package com.victor.picpay.controllers;

import com.victor.picpay.dtos.UserDTO;
import com.victor.picpay.entities.User;
import com.victor.picpay.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createWallet(@RequestBody UserDTO userDTO) {
        var user = userService.createUser(userDTO);
        return ResponseEntity.ok(user);
    }
}

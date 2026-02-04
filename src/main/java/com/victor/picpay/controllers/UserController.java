package com.victor.picpay.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victor.picpay.dtos.UserDTO;
import com.victor.picpay.dtos.UserInfoDTO;
import com.victor.picpay.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserInfoDTO> createUser(@RequestBody UserDTO userDTO) {
        var user = userService.createUser(userDTO);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoDTO> showInfo(@PathVariable(name = "id") String userId) {
        var user = userService.fetchUserInfo(UUID.fromString(userId));
        return ResponseEntity.ok().body(user);
    };
}

package com.victor.picpay.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victor.picpay.dtos.SimpleMessageDTO;
import com.victor.picpay.dtos.UpdateUserDTO;
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
    public ResponseEntity<UserInfoDTO> showInfo(@PathVariable("id") String userId) {
        var user = userService.fetchUserInfo(UUID.fromString(userId));
        return ResponseEntity.ok().body(user);
    };

    @PutMapping("/update/{id}")
    public ResponseEntity<SimpleMessageDTO> update(@PathVariable("id") String userId, @RequestBody UpdateUserDTO dto) {
        return ResponseEntity.ok().body(userService.updateUser(userId, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SimpleMessageDTO> delete(@PathVariable("id") String userId) {
        return ResponseEntity.ok().body(userService.deleterUser(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserInfoDTO>> fetchAllUsers() {
        var usersList = userService.fetchAllUsersInfo();
        return ResponseEntity.ok().body(usersList);
    }
}

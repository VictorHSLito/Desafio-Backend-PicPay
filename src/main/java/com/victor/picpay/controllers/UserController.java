package com.victor.picpay.controllers;

import com.victor.picpay.dtos.requests.UpdateUserDTO;
import com.victor.picpay.dtos.requests.UserDTO;
import com.victor.picpay.dtos.responses.SimpleMessageDTO;
import com.victor.picpay.dtos.responses.UserInfoDTO;
import com.victor.picpay.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserInfoDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        var user = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoDTO> showInfo(@PathVariable("id") UUID userId) {
        var user = userService.fetchUserInfo(userId);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleMessageDTO> update(@PathVariable("id") UUID userId, @RequestBody @Valid UpdateUserDTO dto) {
        return ResponseEntity.ok().body(userService.updateUser(userId, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleMessageDTO> delete(@PathVariable("id") UUID userId) {
        return ResponseEntity.ok().body(userService.deleteUser(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserInfoDTO>> fetchAllUsers() {
        var usersList = userService.fetchAllUsersInfo();
        return ResponseEntity.ok().body(usersList);
    }
}

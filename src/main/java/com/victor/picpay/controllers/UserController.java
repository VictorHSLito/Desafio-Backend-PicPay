package com.victor.picpay.controllers;

import com.victor.picpay.dtos.requests.UpdateUserDTO;
import com.victor.picpay.dtos.requests.UserDTO;
import com.victor.picpay.dtos.responses.SimpleMessageDTO;
import com.victor.picpay.dtos.responses.UserInfoDTO;
import com.victor.picpay.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@Tag(name = "Usuários", description = "Responsável pelo gerenciamento de usuários")
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

    @SecurityRequirement(name = "Bearer Token")
    @GetMapping("/{id}")
    public ResponseEntity<UserInfoDTO> showInfo(@PathVariable("id") UUID userId) {
        var user = userService.fetchUserInfo(userId);
        return ResponseEntity.ok().body(user);
    }

    @SecurityRequirement(name = "Bearer Token")
    @PutMapping("/{id}")
    public ResponseEntity<SimpleMessageDTO> update(@PathVariable("id") UUID userId, @RequestBody @Valid UpdateUserDTO dto) {
        return ResponseEntity.ok().body(userService.updateUser(userId, dto));
    }

    @SecurityRequirement(name = "Bearer Token")
    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleMessageDTO> delete(@PathVariable("id") UUID userId) {
        return ResponseEntity.ok().body(userService.deleteUser(userId));
    }

    @SecurityRequirement(name = "Bearer Token")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserInfoDTO>> fetchAllUsers(Pageable pageable) {
        var usersList = userService.fetchAllUsersInfo(pageable);
        return ResponseEntity.ok().body(usersList);
    }
}

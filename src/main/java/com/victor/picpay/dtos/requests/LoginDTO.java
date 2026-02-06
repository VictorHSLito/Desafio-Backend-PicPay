package com.victor.picpay.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @NotBlank
        @Email(message = "${spring.validation.email}")
        String email,

        @NotBlank
        @Size(min = 2, message = "${spring.validation.password}")
        String password) {}

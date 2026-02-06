package com.victor.picpay.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        @Size(min = 2, message = "{spring.validation.firstname}")
        String firstName,

        String lastName,

        @Email(message = "{spring.validation.email}")
        String email,

        @Size(min = 5, message = "{spring.validation.password}")
        String password
) {}

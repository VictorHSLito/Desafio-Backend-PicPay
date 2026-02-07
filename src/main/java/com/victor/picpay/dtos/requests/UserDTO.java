package com.victor.picpay.dtos.requests;

import com.victor.picpay.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotBlank
        @Size(min = 2, message = "{spring.validation.firstname}")
        String firstName,

        String lastName,

        @NotBlank
        @Size(min = 11, message = "{spring.validation.cpfcnpj}")
        String cpfCnpj,

        @NotBlank
        @Email(message = "{spring.validation.email}" )
        String email,

        @NotBlank
        @Size(min = 5, message = "{spring.validation.password}")
        String password,

        @NotBlank(groups = UserType.class)
        UserType userType) {}

package com.victor.picpay.dtos.requests;

import com.victor.picpay.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotBlank
        @Size(min = 2, message = "{spring.validation.firstname}")
        @Schema(example = "admin")
        String firstName,

        String lastName,

        @NotBlank
        @Size(min = 11, message = "{spring.validation.cpfcnpj}")
        @Schema(example = "99999999999")
        String cpfCnpj,

        @NotBlank
        @Email(message = "{spring.validation.email}" )
        @Schema(example = "admin@gmail.com")
        String email,

        @NotBlank
        @Size(min = 5, message = "{spring.validation.password}")
        String password,

        @NotBlank(groups = UserType.class)
        @Schema(example = "ADMIN")
        UserType userType) {}

package com.victor.picpay.dtos;

import com.victor.picpay.entities.User;
import com.victor.picpay.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotEmpty(message = "O campo [firstName] é obrigatório e não pode ser vazio!")
        @Size(min = 2, max = 15, message = "O campo [firstName] deve conter entre [2-15] caracteres!")
        String firstName,
        String lastName,

        @NotBlank(message = "O campo [cpfCnpj] é obrigatório")
        String cpfCnpj,

        @Email(regexp = "^(.+)@(\\S+)$", message = "O campo [email] deve ser um email válido!")
        String email,

        @NotBlank(message = "O campo [password] não pode ser vazio")
        String password,

        UserType userType) {

    public static User toUser(UserDTO userDTO) {
        return new User(
                userDTO.firstName(),
                userDTO.lastName(),
                userDTO.cpfCnpj(),
                userDTO.email(),
                userDTO.password(),
                userDTO.userType()
        );
    }
}

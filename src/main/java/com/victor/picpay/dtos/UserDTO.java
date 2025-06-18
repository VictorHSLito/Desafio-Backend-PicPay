package com.victor.picpay.dtos;

import com.victor.picpay.entities.User;
import com.victor.picpay.enums.UserType;

public record UserDTO(String firstName,
                      String lastName,
                      String cpfCnpj,
                      String email,
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

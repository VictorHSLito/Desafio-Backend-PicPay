package com.victor.picpay.dtos;

import com.victor.picpay.enums.UserType;

public record UserDTO(String firstName,
                      String lastName,
                      String cpfCnpj,
                      String email,
                      String password,
                      UserType userType) {}

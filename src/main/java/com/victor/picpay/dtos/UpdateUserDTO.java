package com.victor.picpay.dtos;

public record UpdateUserDTO(
    String firstName,
    String lastName,
    String email,
    String password
) {}

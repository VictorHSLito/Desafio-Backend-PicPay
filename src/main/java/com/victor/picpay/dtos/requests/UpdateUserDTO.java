package com.victor.picpay.dtos.requests;

public record UpdateUserDTO(
    String firstName,
    String lastName,
    String email,
    String password
) {}

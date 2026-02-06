package com.victor.picpay.dtos.responses;

import java.util.UUID;

import com.victor.picpay.enums.UserType;

public record UserInfoDTO(
    UUID userId,
    String firstName,
    String lastName,
    UserType userType
) {}

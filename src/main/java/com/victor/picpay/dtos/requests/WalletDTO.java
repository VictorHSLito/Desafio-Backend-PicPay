package com.victor.picpay.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDTO(
        @Positive(message = "{spring.validation.balance}")
        BigDecimal balance,

        @NotNull(message = "{spring.validation.userid}")
        UUID userId) {
}

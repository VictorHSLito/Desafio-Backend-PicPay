package com.victor.picpay.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDTO(
        @NotBlank(message = "{spring.validation.payer}")
        UUID payer,

        @NotBlank(message = "{spring.validation.payee}")
        UUID payee,

        @NotNull
        @Positive(message = "{spring.validation.value}")
        BigDecimal value
) {}

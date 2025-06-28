package com.victor.picpay.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDTO(
        UUID payer,
        UUID payee,
        BigDecimal value
) {}

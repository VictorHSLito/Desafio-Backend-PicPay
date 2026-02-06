package com.victor.picpay.dtos.requests;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDTO (BigDecimal balance,
                         UUID userId) {}

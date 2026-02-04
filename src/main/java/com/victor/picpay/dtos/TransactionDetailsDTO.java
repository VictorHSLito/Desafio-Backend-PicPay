package com.victor.picpay.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDetailsDTO(
    String payerName,
    String payeeName,
    BigDecimal value,
    LocalDateTime date
) {}

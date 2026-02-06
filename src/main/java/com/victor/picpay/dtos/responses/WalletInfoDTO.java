package com.victor.picpay.dtos.responses;

import java.math.BigDecimal;

public record WalletInfoDTO(
    BigDecimal balance,
    String userFirstName,
    String cpfCnpj,
    String userType
) {}

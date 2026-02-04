package com.victor.picpay.dtos;

import java.math.BigDecimal;

public record WalletInfoDTO(
    BigDecimal balance,
    String userFirstName,
    String cpfCnpj,
    String userType
) {}

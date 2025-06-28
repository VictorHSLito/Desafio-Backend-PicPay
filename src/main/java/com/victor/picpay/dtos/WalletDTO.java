package com.victor.picpay.dtos;

import com.victor.picpay.entities.User;
import com.victor.picpay.entities.Wallet;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDTO (BigDecimal balance,
                         UUID userId) {

    public Wallet fromDTOtoWallet(User user) {
        return new Wallet(
                null,
                this.balance(),
                user
        );
    }
}

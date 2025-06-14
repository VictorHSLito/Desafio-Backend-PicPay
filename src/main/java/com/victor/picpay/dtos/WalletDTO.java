package com.victor.picpay.dtos;

import com.victor.picpay.entities.Wallet;
import com.victor.picpay.enums.WalletType;

public record WalletDTO(String firsName,
                        String lastName,
                        String email,
                        String password,
                        WalletType walletType) {

    public Wallet toWallet() {
        return new Wallet(
          firsName,
          lastName,
          email,
          password,
          walletType
        );
    }
}

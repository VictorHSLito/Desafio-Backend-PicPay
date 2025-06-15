package com.victor.picpay.dtos;

import com.victor.picpay.entities.Wallet;
import com.victor.picpay.enums.WalletType;

public record WalletDTO(String firstName,
                        String lastName,
                        String cpfCnpj,
                        String email,
                        String password,
                        WalletType walletType) {

    public Wallet toWallet() {
        return new Wallet(
          firstName(),
          lastName(),
          cpfCnpj(),
          email(),
          password(),
          walletType()
        );
    }
}

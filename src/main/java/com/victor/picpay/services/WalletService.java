package com.victor.picpay.services;

import com.victor.picpay.entities.Wallet;
import com.victor.picpay.repositories.WalletRespository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    private final WalletRespository walletRespository;

    public WalletService(WalletRespository walletRespository) {
        this.walletRespository = walletRespository;
    }

    public void save(Wallet wallet) {
        walletRespository.save(wallet);
    }
}

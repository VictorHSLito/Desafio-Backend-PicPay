package com.victor.picpay.services;

import com.victor.picpay.dtos.WalletDTO;
import com.victor.picpay.entities.Wallet;
import com.victor.picpay.exceptions.WalletDataAlreadyExists;
import com.victor.picpay.repositories.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(WalletDTO walletDTO) {

        var wallet = walletRepository.findByCpfCnpjOrEmail(walletDTO.email(), walletDTO.cpfCnpj());
        if (wallet.isPresent()) {
            throw new WalletDataAlreadyExists("CPF or CNPJ already exists");
        }
        return walletRepository.save(walletDTO.toWallet());
    }
}

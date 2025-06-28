package com.victor.picpay.services;

import com.victor.picpay.dtos.WalletDTO;
import com.victor.picpay.entities.User;
import com.victor.picpay.entities.Wallet;
import com.victor.picpay.repositories.UserRepository;
import com.victor.picpay.repositories.WalletRespository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    private final WalletRespository walletRespository;
    private final UserRepository userRepository;

    public WalletService(WalletRespository walletRespository, UserRepository userRepository) {
        this.walletRespository = walletRespository;
        this.userRepository = userRepository;
    }

    public void save(WalletDTO walletDTO) {
        User user = userRepository.findById(walletDTO.userId()).orElseThrow(() -> new RuntimeException("User not found!"));
        Wallet wallet = walletDTO.fromDTOtoWallet(user);
        walletRespository.save(wallet);
    }

    public void save(Wallet wallet) {
        walletRespository.save(wallet);}
}

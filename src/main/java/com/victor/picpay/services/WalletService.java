package com.victor.picpay.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.victor.picpay.dtos.WalletDTO;
import com.victor.picpay.entities.Wallet;
import com.victor.picpay.exceptions.UserNotFound;
import com.victor.picpay.mappers.WalletMapper;
import com.victor.picpay.repositories.UserRepository;
import com.victor.picpay.repositories.WalletRespository;

@Service
public class WalletService {
    private final WalletRespository walletRespository;

    private final UserRepository userRepository;

    private final WalletMapper walletMapper;

    public WalletService(WalletRespository walletRespository, UserRepository userRepository, WalletMapper walletMapper) {
        this.walletRespository = walletRespository;
        this.userRepository = userRepository;
        this.walletMapper = walletMapper;
    }

    public void save(WalletDTO walletDTO) {

        UUID userId = walletDTO.userId();

        if (verifyIfUserExists(userId)) {
            Wallet wallet = walletMapper.dtoToWallet(walletDTO);
            walletRespository.save(wallet);
        }
    }

    public void save(Wallet wallet) {
        walletRespository.save(wallet);
    }

    private boolean verifyIfUserExists(UUID userId) throws UserNotFound {
        userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFound("User not found!"));

        return true;
    }
}

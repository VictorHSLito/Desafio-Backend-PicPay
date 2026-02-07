package com.victor.picpay.services;

import java.util.UUID;

import com.victor.picpay.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.victor.picpay.dtos.requests.WalletDTO;
import com.victor.picpay.dtos.responses.WalletInfoDTO;
import com.victor.picpay.entities.Wallet;
import com.victor.picpay.exceptions.UserNotFoundException;
import com.victor.picpay.exceptions.WalletNotFoundException;
import com.victor.picpay.mappers.WalletMapper;
import com.victor.picpay.repositories.UserRepository;
import com.victor.picpay.repositories.WalletRepository;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    private final UserRepository userRepository;

    private final WalletMapper walletMapper;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository, WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.walletMapper = walletMapper;
    }

    @Transactional
    public void save(WalletDTO walletDTO) {
        UUID userId = walletDTO.userId();

        var user = verifyIfUserExists(userId);

        if (user.getUserWallet() != null) {
            throw new IllegalStateException("User already has a wallet!");
        }

        Wallet wallet = walletMapper.dtoToWallet(walletDTO);

        wallet.setUser(user);

        walletRepository.save(wallet);
    }

    public void save(Wallet wallet) {
        walletRepository.save(wallet);
    }

    private User verifyIfUserExists(UUID userId) {
        return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    public WalletInfoDTO getWalletIfOwner(UUID walletId, String authenticatedUserEmail){
        // 1. Busca a wallet
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found!"));

        // 2. Verifica se o dono da wallet é o mesmo do token
        // Assumindo que o subject do seu JWT seja o email ou CPF
        if (!wallet.getUser().getEmail().equals(authenticatedUserEmail)) {
            throw new AccessDeniedException("You do not have permission to access this wallet.");
        }

        return walletMapper.fromWalletToDto(wallet);
    }
}

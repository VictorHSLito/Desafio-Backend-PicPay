package com.victor.picpay.services;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.victor.picpay.dtos.WalletDTO;
import com.victor.picpay.dtos.WalletInfoDTO;
import com.victor.picpay.entities.Wallet;
import com.victor.picpay.exceptions.UserNotFoundException;
import com.victor.picpay.exceptions.WalletNotFoundException;
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

    private boolean verifyIfUserExists(UUID userId) throws UserNotFoundException {
        userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User not found!"));

        return true;
    }

    public WalletInfoDTO getWalletIfOwner(UUID walletId, String authenticatedUserEmail) throws AccessDeniedException {
        // 1. Busca a wallet
        Wallet wallet = walletRespository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found!"));

        // 2. Verifica se o dono da wallet é o mesmo do token
        // Assumindo que o subject do seu JWT seja o email ou CPF
        if (!wallet.getUser().getEmail().equals(authenticatedUserEmail)) {
            throw new AccessDeniedException("You do not have permission to access this wallet.");
        }

        return walletMapper.fromWalletToDto(wallet);

    }
}

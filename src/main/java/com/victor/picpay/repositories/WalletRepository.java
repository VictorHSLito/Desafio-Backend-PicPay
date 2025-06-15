package com.victor.picpay.repositories;

import com.victor.picpay.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findByCpfCnpjOrEmail(String email, String cpfCnpj);
}

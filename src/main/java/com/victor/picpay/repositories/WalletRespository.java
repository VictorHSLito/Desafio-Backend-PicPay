package com.victor.picpay.repositories;

import com.victor.picpay.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletRespository extends JpaRepository<Wallet, UUID> {
}

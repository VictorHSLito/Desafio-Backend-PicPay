package com.victor.picpay.controllers;

import com.victor.picpay.dtos.WalletDTO;
import com.victor.picpay.entities.Wallet;
import com.victor.picpay.services.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/create")
    public ResponseEntity<Wallet> createWallet(@RequestBody WalletDTO walletDTO) {
        var wallet = walletService.createWallet(walletDTO);
        return ResponseEntity.ok(wallet);
    }
}

package com.victor.picpay.controllers;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victor.picpay.dtos.requests.WalletDTO;
import com.victor.picpay.dtos.responses.WalletInfoDTO;
import com.victor.picpay.services.WalletService;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createWallet(@RequestBody @Valid WalletDTO walletDTO) {
        walletService.save(walletDTO);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletInfoDTO> showWallet(@PathVariable UUID walletId, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException{

        String userEmail = userDetails.getUsername();

        var walletDTO = walletService.getWalletIfOwner(walletId, userEmail);
        return ResponseEntity.ok().body(walletDTO);
    }
}

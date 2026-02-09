package com.victor.picpay.controllers;

import com.victor.picpay.dtos.requests.WalletDTO;
import com.victor.picpay.dtos.responses.WalletInfoDTO;
import com.victor.picpay.services.WalletService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/wallet")
@SecurityRequirement(name = "Bearer Token")
@Tag(name = "Carteiras", description = "Responsável pela criação e visualização de carteiras")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Void> createWallet(@RequestBody @Valid WalletDTO walletDTO) {
        walletService.save(walletDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletInfoDTO> showWallet(@PathVariable UUID walletId, @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();

        var walletDTO = walletService.getWalletIfOwner(walletId, userEmail);

        return ResponseEntity.ok().body(walletDTO);
    }
}

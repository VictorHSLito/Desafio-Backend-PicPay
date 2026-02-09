package com.victor.picpay.controllers;

import com.victor.picpay.dtos.requests.TransactionDTO;
import com.victor.picpay.dtos.responses.TransactionDetailsDTO;
import com.victor.picpay.services.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transfer")
@SecurityRequirement(name = "Bearer Token")
@Tag(name = "Transações", description = "Responsável pelas transações dos usuários")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDetailsDTO> realizeTransfer(@RequestBody @Valid TransactionDTO transactionDTO) {
        var transactionDetails = transactionService.executeTransferation(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDetails);
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<TransactionDetailsDTO>> showTransactions(@PathVariable UUID userId, @AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();

        var listOfTransactions = transactionService.fetchAllTransactions(userId, userEmail);

        return ResponseEntity.ok().body(listOfTransactions);
    }
}

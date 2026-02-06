package com.victor.picpay.controllers;

import java.nio.file.AccessDeniedException;
import java.util.List;

import jakarta.transaction.Transactional;
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

import com.victor.picpay.dtos.requests.TransactionDTO;
import com.victor.picpay.dtos.responses.TransactionDetailsDTO;
import com.victor.picpay.services.TransactionService;

@RestController
@RequestMapping("/transfer")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<Void> realizeTransfer(@RequestBody @Valid TransactionDTO transactionDTO) {
        transactionService.executeTransferation(transactionDTO);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<TransactionDetailsDTO>> showTransactions(@PathVariable String userId, @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
        String userEmail = userDetails.getUsername();

        var listOfTransactions = transactionService.fetchAllTransactions(userId, userEmail);

        return ResponseEntity.ok().body(listOfTransactions);
    }
}

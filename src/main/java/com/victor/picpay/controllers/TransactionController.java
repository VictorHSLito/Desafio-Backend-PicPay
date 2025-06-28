package com.victor.picpay.controllers;

import com.victor.picpay.dtos.TransactionDTO;
import com.victor.picpay.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/")
    public ResponseEntity<Void> realizeTransfer(@RequestBody TransactionDTO transactionDTO) {
        transactionService.executeTransferation(transactionDTO);
        return ResponseEntity.accepted().build();
    }
}

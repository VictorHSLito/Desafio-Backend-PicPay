package com.victor.picpay.services;

import com.victor.picpay.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TrasactionService {

    private final TransactionRepository transactionRepository;

    public TrasactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}

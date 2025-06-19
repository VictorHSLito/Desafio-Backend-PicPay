package com.victor.picpay.services;

import com.victor.picpay.dtos.TrasactionDTO;
import com.victor.picpay.entities.Transaction;
import com.victor.picpay.entities.User;
import com.victor.picpay.repositories.TransactionRepository;
import com.victor.picpay.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TrasactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public TrasactionService(TransactionRepository transactionRepository, UserRepository userRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Transactional
    public void executeTransferation(TrasactionDTO trasactionDTO) {
        var payer = userService.findUser(trasactionDTO.payer().getId());
        var payee = userService.findUser(trasactionDTO.payee().getId());

        if (UserService.verifyUserType(payer)) {
            throw new IllegalArgumentException("Payer user cannot be MERCHANT!!!");
        }

        verifyBalance(payer, trasactionDTO.value());
    }


    private void verifyBalance(User user, BigDecimal value) {
        if (user.getUserWallet().getBalance().compareTo(value) < 0) {
            throw new IllegalArgumentException("User doesn't have enough balance to this transaction!");
        }
    }

}

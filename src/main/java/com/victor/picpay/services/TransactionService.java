package com.victor.picpay.services;

import com.victor.picpay.clients.services.AuthorizationService;
import com.victor.picpay.clients.services.NotificationService;
import com.victor.picpay.dtos.TransactionDTO;
import com.victor.picpay.entities.Transaction;
import com.victor.picpay.entities.User;
import com.victor.picpay.entities.Wallet;
import com.victor.picpay.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final WalletService walletService;
    private final NotificationService notificationService;

    @Transactional
    public void executeTransferation(TransactionDTO transactionDTO) {
        var payer = userService.findUser(transactionDTO.payer());
        var payee = userService.findUser(transactionDTO.payee());

        if (userService.verifyUserType(payer)) {
            throw new IllegalArgumentException("Payer user cannot be MERCHANT!!!");
        }

        verifyBalance(payer, transactionDTO.value());
        validateTransfer();

        BigDecimal newBalancePayer = payer.getUserWallet().getBalance().subtract(transactionDTO.value());
        BigDecimal newBalancePayee = payee.getUserWallet().getBalance().add(transactionDTO.value());

        payer.getUserWallet().setBalance(newBalancePayer);
        payee.getUserWallet().setBalance(newBalancePayee);

        updateWallet(payer.getUserWallet());
        updateWallet(payee.getUserWallet());

        Transaction transaction = Transaction.builder()
                .value(transactionDTO.value())
                .payer(payer)
                .payee(payee)
                .build();

        transactionRepository.save(transaction);

        notificationService.sendNotification();
    }


    private void verifyBalance(User user, BigDecimal value) throws IllegalArgumentException{
        if (user.getUserWallet().getBalance().compareTo(value) < 0) {
            throw new IllegalArgumentException("User doesn't have enough balance to this transaction!");
        }
    }

    private void validateTransfer() throws IllegalArgumentException {
        if (!authorizationService.validateTransfer()) {
            throw new IllegalArgumentException("Transaction couldn't be completed by API");
        }
    }

    private void updateWallet(Wallet wallet) {
        walletService.save(wallet);
    }
}

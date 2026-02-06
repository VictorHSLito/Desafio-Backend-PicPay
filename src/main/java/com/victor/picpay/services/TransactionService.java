package com.victor.picpay.services;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.victor.picpay.clients.services.AuthorizationService;
import com.victor.picpay.clients.services.NotificationService;
import com.victor.picpay.dtos.requests.TransactionDTO;
import com.victor.picpay.dtos.responses.TransactionDetailsDTO;
import com.victor.picpay.entities.Transaction;
import com.victor.picpay.entities.User;
import com.victor.picpay.entities.Wallet;
import com.victor.picpay.exceptions.TransactionNotFoundException;
import com.victor.picpay.exceptions.UserNotFoundException;
import com.victor.picpay.mappers.TransactionMapper;
import com.victor.picpay.repositories.TransactionRepository;
import com.victor.picpay.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final WalletService walletService;
    private final NotificationService notificationService;
    private final TransactionMapper transactionMapper;
    private final UserRepository userRepository;

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


    public List<TransactionDetailsDTO> fetchAllTransactions(String userId, String userEmail) throws AccessDeniedException {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User not found!"));

        if (!user.getId().toString().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to do this operation!");
        }

        List<Transaction> listTransaction = transactionRepository.findByPayerEmail(userEmail);

        if (listTransaction.isEmpty()) {
           throw new TransactionNotFoundException("No transactions found for this user.");
        }

        
        return listTransaction.stream().map(transactionMapper::toDetailsDTO).toList();
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

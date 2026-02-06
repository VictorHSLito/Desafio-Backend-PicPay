package com.victor.picpay.services;

import com.victor.picpay.clients.services.AuthorizationService;
import com.victor.picpay.clients.services.NotificationService;
import com.victor.picpay.dtos.requests.TransactionDTO;
import com.victor.picpay.dtos.responses.TransactionDetailsDTO;
import com.victor.picpay.entities.Transaction;
import com.victor.picpay.entities.User;
import com.victor.picpay.entities.Wallet;
import com.victor.picpay.enums.UserType;
import com.victor.picpay.mappers.TransactionMapper;
import com.victor.picpay.repositories.TransactionRepository;
import com.victor.picpay.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private UserService userService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private TransactionService transactionService;

    @Nested
    class ExecuteTransactionTest {
        @Test
        @DisplayName("Should Execute An Transaction Successfully")
        void shouldExecuteAnTransactionSuccessfully() {
            // Arrange
            UUID payerUUID = UUID.randomUUID();
            UUID payeeUUID = UUID.randomUUID();

            User payer = User.builder()
            .firstName("Fist name payer")
            .lastName("Last name payer")
            .cpfCnpj("00000000000")
            .email("payer.test@gmail.com")
            .password("anyPassword#123")
            .userType(UserType.REGULAR)
            .build();

            payer.setId(payerUUID);
            Wallet payerWallet = new Wallet(UUID.randomUUID(), BigDecimal.valueOf(2000), payer);
            payer.setUserWallet(payerWallet);


            User payee = User.builder()
            .firstName("Fist name payee")
            .lastName("Last name payee")
            .cpfCnpj("00000000001")
            .email("payee.test@gmail.com")
            .password("anyPassword#1234")
            .userType(UserType.MERCHANT)
            .build();

            payee.setId(payeeUUID);
            Wallet payeeWallet = new Wallet(UUID.randomUUID(), BigDecimal.valueOf(0), payee);
            payee.setUserWallet(payeeWallet);

            TransactionDTO transactionDTO = new TransactionDTO(
                    payerUUID,
                    payeeUUID,
                    BigDecimal.valueOf(2000)
            );

            doReturn(payer).when(userService).findUser(payerUUID);

            doReturn(payee).when(userService).findUser(payeeUUID);

            doReturn(false).when(userService).verifyUserType(payer);

            doReturn(true).when(authorizationService).validateTransfer();

            doNothing().when(walletService).save(any(Wallet.class));

            doReturn(Transaction.builder().build()).when(transactionRepository).save(any(Transaction.class));

            doNothing().when(notificationService).sendNotification();

            // Act

            transactionService.executeTransferation(transactionDTO);

            // Assert
            verify(userService, times(1)).findUser(payerUUID);
            verify(userService, times(1)).findUser(payeeUUID);
            verify(userService, times(1)).verifyUserType(payer);
            verify(authorizationService, times(1)).validateTransfer();

            assertEquals(BigDecimal.valueOf(0), payer.getUserWallet().getBalance());
            assertEquals(BigDecimal.valueOf(2000), payee.getUserWallet().getBalance());

            verify(walletService, times(2)).save(any(Wallet.class));
            verify(transactionRepository, times(1)).save(any(Transaction.class));
            verify(notificationService, times(1)).sendNotification();
        }

        @Test
        @DisplayName("Should Throws Exception When User Type Is Merchant")
        void shouldThrowsExceptionWhenUserTypeIsMerchant() {
            UUID payerUUID = UUID.randomUUID();
            UUID payeeUUID = UUID.randomUUID();

            User payer = User.builder()
            .firstName("First name payer")
            .lastName("Last name payer")
            .cpfCnpj("00000000000")
            .email("payer.test@gmail.com")
            .password("anyPassword#123")
            .userType(UserType.MERCHANT)
            .build();

            payer.setId(payerUUID);
            Wallet payerWallet = new Wallet(UUID.randomUUID(), BigDecimal.valueOf(2000), payer);
            payer.setUserWallet(payerWallet);

            User payee = User.builder()
            .firstName("First name payer")
            .lastName("Last name payer")
            .cpfCnpj("00000000001")
            .email("payee.test@gmail.com")
            .password("anyPassword#123")
            .userType(UserType.MERCHANT)
            .build();

            payee.setId(payeeUUID);
            Wallet payeeWallet = new Wallet(UUID.randomUUID(), BigDecimal.valueOf(0), payee);
            payee.setUserWallet(payeeWallet);

            TransactionDTO transactionDTO = new TransactionDTO(
                    payerUUID,
                    payeeUUID,
                    BigDecimal.valueOf(2000)
            );

            doReturn(payer).when(userService).findUser(payerUUID);
            doReturn(payee).when(userService).findUser(payeeUUID);

            doReturn(true).when(userService).verifyUserType(payer);

            assertThrows(IllegalArgumentException.class, () -> transactionService.executeTransferation(transactionDTO),
                    "Payer user cannot be MERCHANT!!!");
            verify(authorizationService, never()).validateTransfer();
            verify(walletService, never()).save(any(Wallet.class));
            verify(transactionRepository, never()).save(any(Transaction.class));
            verify(notificationService, never()).sendNotification();
        }

        @Test
        @DisplayName("Should Not Complete Transaction When Authorization Client Didn't Validated Operation")
        void shouldNotCompleteTransactionWhenAuthorizationClientDidNotValidateOperation() {
            UUID payerUUID = UUID.randomUUID();
            UUID payeeUUID = UUID.randomUUID();

            User payer = User.builder()
            .firstName("First name payer")
            .lastName("Last name payer")
            .cpfCnpj("00000000000")
            .email("payer.test@gmail.com")
            .password("anyPassword#123")
            .userType(UserType.REGULAR)
            .build();

            payer.setId(payerUUID);
            Wallet payerWallet = new Wallet(UUID.randomUUID(), BigDecimal.valueOf(2000), payer);
            payer.setUserWallet(payerWallet);

            
            User payee = User.builder()
            .firstName("First name payer")
            .lastName("Last name payer")
            .cpfCnpj("00000000001")
            .email("payee.test@gmail.com")
            .password("anyPassword#123")
            .userType(UserType.MERCHANT)
            .build();

            payee.setId(payeeUUID);
            Wallet payeeWallet = new Wallet(UUID.randomUUID(), BigDecimal.valueOf(0), payee);
            payee.setUserWallet(payeeWallet);

            TransactionDTO transactionDTO = new TransactionDTO(
                    payerUUID,
                    payeeUUID,
                    BigDecimal.valueOf(2000)
            );

            doReturn(payer).when(userService).findUser(payerUUID);
            doReturn(payee).when(userService).findUser(payeeUUID);

            doReturn(false).when(userService).verifyUserType(payer);

            doReturn(false).when(authorizationService).validateTransfer();

            assertThrows(IllegalArgumentException.class, () -> transactionService.executeTransferation(transactionDTO),
                    "Transaction couldn't be completed by API");

            verify(transactionRepository, never()).save(any(Transaction.class));
            verify(notificationService, never()).sendNotification();
            verify(walletService, never()).save(any(Wallet.class));
        }
    }

    @Nested
    class FindTransactions {
        @Test
        @DisplayName("Should Show All User Transactions Correctly")
        void shouldShowAllUserTransactionsCorrectly() throws AccessDeniedException {
            UUID userId = UUID.randomUUID();

            User userPayer = User.builder().id(userId).firstName("Test Payer").build();
            User userPayee = User.builder().firstName("Test Payee").build();

            Transaction transaction1 = Transaction.builder()
                    .payer(userPayer)
                    .payee(userPayee)
                    .value(BigDecimal.valueOf(100L))
                    .build();

            Transaction transaction2 = Transaction.builder()
                    .payer(userPayer)
                    .payee(userPayee)
                    .value(BigDecimal.valueOf(200L))
                    .build();

            Transaction transaction3 = Transaction.builder()
                    .payer(userPayer)
                    .payee(userPayee)
                    .value(BigDecimal.valueOf(300L))
                    .build();

            TransactionDetailsDTO dto1 = new TransactionDetailsDTO("Test Payer", "Test Payee", BigDecimal.valueOf(100L), null);
            TransactionDetailsDTO dto2 = new TransactionDetailsDTO("Test Payer", "Test Payee", BigDecimal.valueOf(200L), null);
            TransactionDetailsDTO dto3 = new TransactionDetailsDTO("Test Payer", "Test Payee", BigDecimal.valueOf(300L), null);

            List<Transaction> listOfTransactions = List.of(transaction1, transaction2, transaction3);

            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userPayer));

            when(transactionRepository.findByPayerEmail(anyString())).thenReturn(listOfTransactions);

            when(transactionMapper.toDetailsDTO(any(Transaction.class))).thenReturn(dto1, dto2, dto3);

            var output = transactionService.fetchAllTransactions(userId.toString(), anyString());

            assertAll("Verify if all fields matches",
                () -> assertEquals("Test Payer", output.get(0).payerName()),
                () -> assertEquals("Test Payee", output.get(0).payeeName()),
                () -> assertEquals(BigDecimal.valueOf(100L), output.get(0).value()),
                () -> assertEquals("Test Payer", output.get(1).payerName()),
                () -> assertEquals("Test Payee", output.get(1).payeeName()),
                () -> assertEquals(BigDecimal.valueOf(200L), output.get(1).value()),
                () -> assertEquals("Test Payer", output.get(2).payerName()),
                () -> assertEquals("Test Payee", output.get(2).payeeName()),
                () -> assertEquals(BigDecimal.valueOf(300L), output.get(2).value())
            );
        }
    }
}
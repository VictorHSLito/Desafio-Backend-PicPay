package com.victor.picpay.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.victor.picpay.dtos.WalletDTO;
import com.victor.picpay.entities.User;
import com.victor.picpay.entities.Wallet;
import com.victor.picpay.enums.UserType;
import com.victor.picpay.mappers.WalletMapper;
import com.victor.picpay.repositories.UserRepository;
import com.victor.picpay.repositories.WalletRespository;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRespository walletRespository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletService walletService;

    @Nested
    class CreateWallet {
        @Test
        @DisplayName("Should Create Wallet Successfully")
        void shouldCreateWalletSuccessfully() {
            UUID userId = UUID.randomUUID();

            User user = User.builder()
            .firstName("First Name Test")
            .lastName("Last Name Test")
            .cpfCnpj("00000000000")
            .email("email.test@gmail.com")
            .password("StrongPassWord#321")
            .userType(UserType.REGULAR)
            .build();

            user.setId(userId);

            WalletDTO walletDTO = new WalletDTO(BigDecimal.valueOf(3000), userId);

            Wallet wallet = new Wallet(); 
            wallet.setUser(user);
            wallet.setBalance(BigDecimal.valueOf(3000));

            doReturn(Optional.of(user)).when(userRepository).findById(userId);

            when(walletMapper.dtoToWallet(walletDTO)).thenReturn(wallet);

            walletService.save(walletDTO);

            verify(userRepository, times(1)).findById(userId);

            ArgumentCaptor<Wallet> walletCaptured = ArgumentCaptor.forClass(Wallet.class);

            verify(walletRespository, times(1)).save(walletCaptured.capture());

            Wallet savedWallet = walletCaptured.getValue();

            assertEquals(user, savedWallet.getUser());

            assertEquals(user.getId(), savedWallet.getUser().getId());
        }


        @Test
        @DisplayName("Should Not Create Wallet When User Doesn't Exists")
        void shouldNotCreateWalletWhenUserDoesNotExists() {
            UUID userId = UUID.randomUUID();

            User user = User.builder()
            .firstName("First Name Test")
            .lastName("Last Name Test")
            .cpfCnpj("00000000000")
            .email("email.test@gmail.com")
            .password("StrongPassWord#321")
            .userType(UserType.REGULAR)
            .build();

            user.setId(userId);

            WalletDTO walletDTO = new WalletDTO(BigDecimal.valueOf(3000), userId);

            doReturn(Optional.empty()).when(userRepository).findById(userId);

            assertThrows(RuntimeException.class, () -> walletService.save(walletDTO),
                    "User not found!");
        }

    }
}
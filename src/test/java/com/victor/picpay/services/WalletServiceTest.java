package com.victor.picpay.services;

import com.victor.picpay.dtos.requests.WalletDTO;
import com.victor.picpay.dtos.responses.WalletInfoDTO;
import com.victor.picpay.entities.User;
import com.victor.picpay.entities.Wallet;
import com.victor.picpay.enums.UserType;
import com.victor.picpay.mappers.WalletMapper;
import com.victor.picpay.repositories.UserRepository;
import com.victor.picpay.repositories.WalletRespository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    @Nested
    class RetrieveWalletInfo {
        @Test
        @DisplayName("Should Allow Only The Owner Get Info About Him Wallet")
        void shouldAllowOnlyTheOwnerGetInfoAboutHimWallet() throws AccessDeniedException {
            UUID walletId = UUID.randomUUID();

            String email = "user-test@gmail.com";

            User owner = User.builder().email(email).firstName("Owner").build();

            Wallet wallet = Wallet.builder().id(walletId).user(owner).build();
            WalletInfoDTO expectedDto = new WalletInfoDTO(BigDecimal.TEN,
                    "Owner",
                    "123",
                    "REGULAR");

            when(walletRespository.findById(walletId)).thenReturn(Optional.of(wallet));
            when(walletMapper.fromWalletToDto(wallet)).thenReturn(expectedDto);

            var output = walletService.getWalletIfOwner(walletId, email);

            assertEquals(expectedDto, output);
            assertAll("Verify if all info matches",
                    () -> assertEquals(expectedDto.balance(), output.balance()),
                    () -> assertEquals(expectedDto.userFirstName(), output.userFirstName()),
                    () -> assertEquals(expectedDto.cpfCnpj(), output.cpfCnpj()),
                    () -> assertEquals(expectedDto.userType(), output.userType())
            );
            verify(walletRespository).findById(walletId);
        }

        @Test
        @DisplayName("Should Throw AccessDeniedException When User Email Is Wrong")
        void shouldThrowAccessDeniedExceptionWhenUserEmailIsWrong() {
            UUID walletId = UUID.randomUUID();

            Wallet wallet = Wallet.builder()
                    .user(User.builder().email("test-email@gmail.com").build())
                    .build();

            when(walletRespository.findById(walletId)).thenReturn(Optional.of(wallet));

            Exception exception = assertThrows(AccessDeniedException.class, () -> walletService.getWalletIfOwner(walletId, anyString()));
            assertEquals("You do not have permission to access this wallet.", exception.getMessage());
        }
    }
}
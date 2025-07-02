package com.victor.picpay.services;

import com.victor.picpay.dtos.UserDTO;
import com.victor.picpay.entities.User;
import com.victor.picpay.enums.UserType;
import com.victor.picpay.exceptions.UserNotFound;
import com.victor.picpay.exceptions.WalletDataAlreadyExists;
import com.victor.picpay.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Nested
    class CreateUserTest {
        @Test
        @DisplayName("Should Create A New User Successfully")
        void shouldCreateANewUserSuccessfully() {
            // Arrange
            User user = new User("First Name Test",
                    "Last Name Test",
                    "00000000000",
                    "email.test@gmail.com",
                    "strongPasswordExample#123",
                    UserType.REGULAR); // Create a user for the test

            UserDTO input = new UserDTO(user.getFirstName(),
                    user.getLastName(),
                    user.getCpfCnpj(),
                    user.getEmail(),
                    user.getPassword(),
                    UserType.REGULAR); // Simulate a DTO with data of test user

            when(userRepository.findByCpfCnpjOrEmail(anyString(), anyString()))
                    .thenReturn(Optional.empty()); // Pass verification to not stop the test

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            // Act
            var output = userService.createUser(input);

            var userCaptured = userArgumentCaptor.getValue();

            // Assert
            assertNotNull(output);
            assertEquals(user.getFirstName(), userCaptured.getFirstName());
            assertEquals(user.getLastName(), userCaptured.getLastName());
            assertEquals(user.getCpfCnpj(), userCaptured.getCpfCnpj());
            assertEquals(user.getEmail(), userCaptured.getEmail());
            assertEquals(user.getPassword(), userCaptured.getPassword());
            assertEquals(user.getUserType(), userCaptured.getUserType());
            verify(userRepository, times(1))
                    .save(any(User.class)); // Verify how many times the repository used save method
        }

        @Test
        @DisplayName("Should Throws Exception When Already Exists An User")
        void shouldThrowsExceptionWhenAlreadyExistsAnUser() {
            UserDTO input = new UserDTO("First Name Test",
                    "Last Name Test",
                    "00000000000",
                    "email.test@gmail.com",
                    "strongPasswordExample#123",
                    UserType.REGULAR);

            when(userRepository.findByCpfCnpjOrEmail(input.email(), input.cpfCnpj()))
                    .thenReturn(Optional.of(new User()));

            assertThrows(WalletDataAlreadyExists.class, () -> userService.createUser(input));
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    class FindUserTest {
        @Test
        @DisplayName("Should Find An User By Id And Return It Successfully")
        void shouldFindAnUserByIdAndReturnItSuccessfully() {
            UUID userId = UUID.randomUUID();
            User user = new User("First Name Test",
                    "Last Name Test",
                    "00000000000",
                    "email.test@gmail.com",
                    "strongPasswordExample#123",
                    UserType.REGULAR);

            user.setId(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            var output = userService.findUser(user.getId());

            assertNotNull(output);
            assertEquals(user.getFirstName(), output.getFirstName());
            assertEquals(user.getLastName(), output.getLastName());
            assertEquals(user.getCpfCnpj(), output.getCpfCnpj());
            assertEquals(user.getEmail(), output.getEmail());
            assertEquals(user.getPassword(), output.getPassword());
            assertEquals(user.getUserType(), output.getUserType());
        }


        @Test
        @DisplayName("Should Throws Exception When Not Found An User By Id Successfully")
        void shouldThrowsExceptionWhenNotFoundAnUserByIdSuccessfully() {
            UUID userId = UUID.randomUUID();

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(UserNotFound.class, () -> userService.findUser(userId));

        }
    }

}
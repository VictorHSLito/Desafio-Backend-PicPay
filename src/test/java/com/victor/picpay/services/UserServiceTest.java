package com.victor.picpay.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.victor.picpay.dtos.requests.UserDTO;
import com.victor.picpay.dtos.responses.UserInfoDTO;
import com.victor.picpay.entities.User;
import com.victor.picpay.enums.UserType;
import com.victor.picpay.exceptions.UserNotFoundException;
import com.victor.picpay.exceptions.WalletDataAlreadyExists;
import com.victor.picpay.mappers.UserMapper;
import com.victor.picpay.repositories.UserRepository;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

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

            UUID userId = UUID.randomUUID();

            User user = User.builder()
            .id(userId)
            .firstName("First Name Test")
            .lastName("Last Name Test")
            .cpfCnpj("00000000000")
            .email("email.test@gmail.com")
            .password("strongPasswordExample#123")
            .userType(UserType.REGULAR)
            .build();

            UserDTO input = new UserDTO(user.getFirstName(),
                    user.getLastName(),
                    user.getCpfCnpj(),
                    user.getEmail(),
                    user.getPassword(),
                    UserType.REGULAR); // Simulate a DTO with data of test user

            UserInfoDTO dto = new UserInfoDTO(
                userId,
                user.getFirstName(),
                user.getLastName(),
                user.getUserType()
            );

            when(userRepository.findByCpfCnpjOrEmail(anyString(), anyString())).thenReturn(Optional.empty());
                    
            when(passwordEncoder.encode(user.getPassword())).thenReturn("encrypted_password");
            
            when(userMapper.dtoToUser(input)).thenReturn(user);

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
                    
            when(userMapper.toInfoDto(user)).thenReturn(dto);

            // Act
            var output = userService.createUser(input);

            var userCaptured = userArgumentCaptor.getValue();

            // Assert
            assertNotNull(output);
            assertAll("Verify If User Was Created",
                () -> assertEquals(user.getFirstName(), userCaptured.getFirstName()),
                () -> assertEquals(user.getLastName(), userCaptured.getLastName()),
                () -> assertEquals(user.getCpfCnpj(), userCaptured.getCpfCnpj()),
                () -> assertEquals(user.getEmail(), userCaptured.getEmail()),
                () -> assertEquals(user.getPassword(), userCaptured.getPassword()),
                () -> assertEquals(user.getUserType(), userCaptured.getUserType())
            );
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
                    .thenReturn(Optional.of(User.builder().build()));

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

            User user = User.builder()
            .firstName("First Name Test")
            .lastName("Last Name Test")
            .cpfCnpj("00000000000")
            .email("email.test@gmail.com")
            .password("strongPasswordExample#123")
            .userType(UserType.REGULAR)
            .build();

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

            assertThrows(UserNotFoundException.class, () -> userService.findUser(userId));

        }
    }

}
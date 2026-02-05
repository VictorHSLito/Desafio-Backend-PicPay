package com.victor.picpay.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.victor.picpay.dtos.UpdateUserDTO;
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

import com.victor.picpay.dtos.UserDTO;
import com.victor.picpay.dtos.UserInfoDTO;
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

        @Test
        @DisplayName("Should Fetch User Information Correctly")
        void shouldFetchUserInformationCorrectly() {
            UUID userId = UUID.randomUUID();

            User user = User.builder()
                    .id(userId)
                    .firstName("First Name Test")
                    .lastName("Last Name Test")
                    .userType(UserType.REGULAR)
                    .build();

            UserInfoDTO expectedDto = new UserInfoDTO(
                    userId,
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUserType()
            );

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            when(userMapper.toInfoDto(user)).thenReturn(expectedDto);

            var output = userService.fetchUserInfo(userId);

            assertEquals(expectedDto, output);
            assertAll("Verify if all fields matches",
                    () -> assertEquals(expectedDto.userId(), output.userId()),
                    () -> assertEquals(expectedDto.firstName(), output.firstName()),
                    () -> assertEquals(expectedDto.lastName(), output.lastName()),
                    () -> assertEquals(expectedDto.userType(), output.userType())
            );
        }

        @Test
        @DisplayName("Should Throw Exception When User Id Is Incorrect")
        void shouldThrowExceptionWhenUserIdIsIncorrect() {
            UUID userId = UUID.randomUUID();

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            Exception exception = assertThrows(UserNotFoundException.class, () -> userService.fetchUserInfo(userId));

            assertEquals("User cannot be found on database!", exception.getMessage());
        }

        @Test
        @DisplayName("Should Return All Users Correctly")
        void shouldReturnAllUsersCorrectly() {
            User user1 = User.builder().build();
            User user2 = User.builder().build();

            List<User> userList = List.of(user1, user2);

            UserInfoDTO dto1 = new UserInfoDTO(UUID.randomUUID(),
                    "First Name Test",
                    "Last Name Test",
                    UserType.REGULAR);
            UserInfoDTO dto2 = new UserInfoDTO(UUID.randomUUID(),
                    "First Name Test",
                    "Last Name Test",
                    UserType.MERCHANT);

            List<UserInfoDTO> expectedList = List.of(dto1, dto2);

            when(userRepository.findAll()).thenReturn(userList);

            when(userMapper.toInfoDto(any(User.class)))
                    .thenReturn(dto1, dto2);

            var output = userService.fetchAllUsersInfo();

            assertEquals(expectedList, output);
            assertEquals(expectedList.size(), output.size());
        }
    }

    @Nested
    class UpdateUser {
        @Test
        @DisplayName("Should Update User Correctly When One Field Is Passed")
        void shouldUpdateUserWhenOneFieldIsPassedCorrectly() {

            UUID userId = UUID.randomUUID();

            User user = User.builder()
                    .id(userId)
                    .firstName("First Name")
                    .build();

            UpdateUserDTO dto = new UpdateUserDTO(
                    "Test User",
                    null,
                    null,
                    null
            );

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            doAnswer(invocation -> {
                UpdateUserDTO dtoArg = invocation.getArgument(0);
                User userArg = invocation.getArgument(1);
                userArg.setFirstName(dtoArg.firstName());
                return null;
            }).when(userMapper).updateUserFromDto(any(UpdateUserDTO.class), any(User.class));

            var output = userService.updateUser(userId.toString(), dto);

            verify(userRepository).save(userArgumentCaptor.capture());

            var userResult = userArgumentCaptor.getValue();

            assertEquals("Campos atualizados com sucesso!", output.message());
            assertEquals("Test User", userResult.getFirstName());
        }

        @Test
        @DisplayName("Should Update User Correctly When Two Fields Is Passed")
        void shouldUpdateUserWhenTwoFieldsIsPassedCorrectly() {

            UUID userId = UUID.randomUUID();

            User user = User.builder()
                    .id(userId)
                    .firstName("First Name")
                    .lastName("Last Name")
                    .build();

            UpdateUserDTO dto = new UpdateUserDTO(
                    "Test User",
                    "Test User",
                    null,
                    null
            );

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            doAnswer(invocation -> {
                UpdateUserDTO dtoArg = invocation.getArgument(0);
                User userArg = invocation.getArgument(1);
                userArg.setFirstName(dtoArg.firstName());
                userArg.setLastName(dto.lastName());
                return null;
            }).when(userMapper).updateUserFromDto(any(UpdateUserDTO.class), any(User.class));

            var output = userService.updateUser(userId.toString(), dto);

            verify(userRepository).save(userArgumentCaptor.capture());

            var userResult = userArgumentCaptor.getValue();

            assertEquals("Campos atualizados com sucesso!", output.message());
            assertEquals("Test User", userResult.getFirstName());
            assertEquals("Test User", userResult.getLastName());
        }

        @Test
        @DisplayName("Should Update User Correctly When All Fields Is Passed")
        void shouldUpdateUserWhenAllFieldIsPassedCorrectly() {
            UUID userId = UUID.randomUUID();

            User user = User.builder()
                    .id(userId)
                    .firstName("First Name")
                    .lastName("Last Name")
                    .email("any.email@gmail.com")
                    .password("anyPassword")
                    .build();

            UpdateUserDTO dto = new UpdateUserDTO(
                    "Test User",
                    "Test User",
                    "user.email@test.com",
                    "myNewPassword"
            );

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            when(passwordEncoder.encode(dto.password())).thenReturn("encrypted_password");

            doAnswer(invocation -> {
                UpdateUserDTO dtoArg = invocation.getArgument(0);
                User userArg = invocation.getArgument(1);
                userArg.setFirstName(dtoArg.firstName());
                userArg.setLastName(dto.lastName());
                userArg.setEmail(dto.email());
                userArg.setPassword(passwordEncoder.encode(dto.password()));
                return null;
            }).when(userMapper).updateUserFromDto(any(UpdateUserDTO.class), any(User.class));

            var output = userService.updateUser(userId.toString(), dto);

            verify(userRepository).save(userArgumentCaptor.capture());

            var userResult = userArgumentCaptor.getValue();

            assertAll("Verify if all fields are correct",
                () -> assertEquals("Campos atualizados com sucesso!", output.message()),
                () -> assertEquals("Test User", userResult.getFirstName()),
                () -> assertEquals("Test User", userResult.getLastName()),
                () -> assertEquals("user.email@test.com", userResult.getEmail()),
                () -> assertEquals("encrypted_password", userResult.getPassword())
            );
        }
    }

    @Nested
    class DeleteUser {
        @Test
        @DisplayName("Should Delete User Correctly")
        void shouldDeleteUserCorrectly() {
            UUID userId = UUID.randomUUID();
            User user = User.builder()
                    .id(userId)
                    .build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            var output = userService.deleterUser(userId.toString());

            verify(userRepository, times(1)).delete(user);
            assertEquals("Usuário deletado com sucesso!", output.message());
        }

        @Test
        @DisplayName("Should Throw UserNotFoundException When User Id Is Wrong")
        void shouldThrowUserNotFoundExceptionWhenUserIdIsWrong() {
            UUID userId = UUID.randomUUID();

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.deleterUser(userId.toString()));

            verify(userRepository, never()).delete(any());
        }
    }

}
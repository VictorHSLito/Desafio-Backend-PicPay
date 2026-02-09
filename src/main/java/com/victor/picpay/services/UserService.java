package com.victor.picpay.services;

import com.victor.picpay.dtos.requests.UpdateUserDTO;
import com.victor.picpay.dtos.requests.UserDTO;
import com.victor.picpay.dtos.responses.SimpleMessageDTO;
import com.victor.picpay.dtos.responses.UserInfoDTO;
import com.victor.picpay.entities.User;
import com.victor.picpay.enums.UserType;
import com.victor.picpay.exceptions.UserDataAlreadyExists;
import com.victor.picpay.exceptions.UserNotFoundException;
import com.victor.picpay.mappers.UserMapper;
import com.victor.picpay.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserInfoDTO createUser(UserDTO userDTO) {
        var userExists = userRepository.findByCpfCnpjOrEmail(userDTO.cpfCnpj(), userDTO.email());

        if (userExists.isPresent()) {
            throw new UserDataAlreadyExists("CPF or CNPJ already exists");
        }

        var encryptedPassword = passwordEncoder.encode(userDTO.password());

        var user = userMapper.dtoToUser(userDTO);

        user.setPassword(encryptedPassword);

        userRepository.save(user);

        return userMapper.toInfoDto(user);
    }

    public User findUser(UUID uuid) {
        return verifyIfUserExists(uuid);
    }

    public UserInfoDTO fetchUserInfo(UUID id) {
        User user = verifyIfUserExists(id);
        return userMapper.toInfoDto(user);
    }

    public Page<UserInfoDTO> fetchAllUsersInfo(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toInfoDto);
    }

    @Transactional
    public SimpleMessageDTO updateUser(UUID userId, UpdateUserDTO userDTO) {
        User user = verifyIfUserExists(userId);
        
        userMapper.updateUserFromDto(userDTO, user);

        if (userDTO.password() != null) {
            String encryptedPassword = passwordEncoder.encode(userDTO.password());
            user.setPassword(encryptedPassword);
        }

        userRepository.save(user);

        return new SimpleMessageDTO("Campos atualizados com sucesso!");
    }

    @Transactional
    public SimpleMessageDTO deleteUser(UUID userId) {
        User user = verifyIfUserExists(userId);

        userRepository.delete(user);

        return new SimpleMessageDTO("Usuário deletado com sucesso!");
    }

    public boolean verifyUserType(User user) {
        return user.getUserType().equals(UserType.MERCHANT) ;
    }

    private User verifyIfUserExists(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User cannot be found"));
    }
}

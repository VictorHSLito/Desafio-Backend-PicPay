package com.victor.picpay.services;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.victor.picpay.dtos.SimpleMessageDTO;
import com.victor.picpay.dtos.UpdateUserDTO;
import com.victor.picpay.dtos.UserDTO;
import com.victor.picpay.dtos.UserInfoDTO;
import com.victor.picpay.entities.User;
import com.victor.picpay.enums.UserType;
import com.victor.picpay.exceptions.UserNotFoundException;
import com.victor.picpay.exceptions.WalletDataAlreadyExists;
import com.victor.picpay.mappers.UserMapper;
import com.victor.picpay.repositories.UserRepository;

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

    public UserInfoDTO createUser(UserDTO userDTO) {
        var wallet = userRepository.findByCpfCnpjOrEmail(userDTO.email(), userDTO.cpfCnpj());
        if (wallet.isPresent()) {
            throw new WalletDataAlreadyExists("CPF or CNPJ already exists");
        }

        var encryptedPassword = passwordEncoder.encode(userDTO.password());

        var user = userMapper.dtoToUser(userDTO);

        user.setPassword(encryptedPassword);

        userRepository.save(user);

        return userMapper.toInfoDto(user);
    }

    public User findUser(UUID uuid) {
        return userRepository
                .findById(uuid)
                .orElseThrow(() ->
                        new UserNotFoundException("User cannot be found on database!"));
    }

    public UserInfoDTO fetchUserInfo(UUID id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User cannot be found on database!"));
        return userMapper.toInfoDto(user);
    }

    public List<UserInfoDTO> fetchAllUsersInfo() {
        return userRepository.findAll().stream().map(userMapper::toInfoDto).toList();
    }

    public SimpleMessageDTO updateUser(String userId, UpdateUserDTO userDTO) {
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new UserNotFoundException("User not found"));
        
        userMapper.updateUserFromDto(userDTO, user);

        if (userDTO.password() != null) {
            String encryptedPassword = passwordEncoder.encode(userDTO.password());
            user.setPassword(encryptedPassword);
        }

        userRepository.save(user);

        return new SimpleMessageDTO("Campos atualizados com sucesso!");
    }

    public SimpleMessageDTO deleterUser(String userId) {
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new UserNotFoundException("User not found!"));

        userRepository.delete(user);

        return new SimpleMessageDTO("Usuário deletado com sucesso!");
    }

    public boolean verifyUserType(User user) {
        return user.getUserType().equals(UserType.MERCHANT) ;
    }

    
}

package com.victor.picpay.services;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.victor.picpay.dtos.UserDTO;
import com.victor.picpay.dtos.UserInfoDTO;
import com.victor.picpay.entities.User;
import com.victor.picpay.enums.UserType;
import com.victor.picpay.exceptions.UserNotFound;
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
                        new UserNotFound("User cannot be found on database!"));
    }

    public UserInfoDTO fetchUserInfo(UUID id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new UserNotFound("User cannot be found on database!"));
        return userMapper.toInfoDto(user);
    }


    public boolean verifyUserType(User user) {
        return user.getUserType().equals(UserType.MERCHANT) ;
    }
}

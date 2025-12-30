package com.victor.picpay.services;

import com.victor.picpay.dtos.UserDTO;
import com.victor.picpay.entities.User;
import com.victor.picpay.enums.UserType;
import com.victor.picpay.exceptions.UserNotFound;
import com.victor.picpay.exceptions.WalletDataAlreadyExists;
import com.victor.picpay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserDTO userDTO) {
        var wallet = userRepository.findByCpfCnpjOrEmail(userDTO.email(), userDTO.cpfCnpj());
        if (wallet.isPresent()) {
            throw new WalletDataAlreadyExists("CPF or CNPJ already exists");
        }

        var encryptedPassword = passwordEncoder.encode(userDTO.password());

        var user = UserDTO.toUser(userDTO);

        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    public User findUser(UUID uuid) {
        return userRepository
                .findById(uuid)
                .orElseThrow(() ->
                        new UserNotFound("User cannot be found on database!"));
    }


    public boolean verifyUserType(User user) {
        return user.getUserType().equals(UserType.MERCHANT) ;
    }
}

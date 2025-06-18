package com.victor.picpay.services;

import com.victor.picpay.dtos.UserDTO;
import com.victor.picpay.entities.User;
import com.victor.picpay.exceptions.WalletDataAlreadyExists;
import com.victor.picpay.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserDTO userDTO) {

        var wallet = userRepository.findByCpfCnpjOrEmail(userDTO.email(), userDTO.cpfCnpj());
        if (wallet.isPresent()) {
            throw new WalletDataAlreadyExists("CPF or CNPJ already exists");
        }
        return userRepository.save(UserDTO.toUser(userDTO));
    }
}

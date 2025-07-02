package com.victor.picpay.services;

import com.victor.picpay.dtos.UserDTO;
import com.victor.picpay.entities.User;
import com.victor.picpay.enums.UserType;
import com.victor.picpay.exceptions.UserNotFound;
import com.victor.picpay.exceptions.WalletDataAlreadyExists;
import com.victor.picpay.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

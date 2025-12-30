package com.victor.picpay.repositories;

import com.victor.picpay.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByCpfCnpjOrEmail(String email, String cpfCnpj);
    Optional<User> findUserByEmail(String email);
}

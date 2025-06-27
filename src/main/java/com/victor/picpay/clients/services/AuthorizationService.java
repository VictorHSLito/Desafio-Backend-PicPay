package com.victor.picpay.clients.services;

import com.victor.picpay.clients.AuthorizationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private final AuthorizationClient authorizationClient;

    public boolean validateTransfer() {
        return Objects.equals(authorizationClient.validateAuthorization().data().authorization(), "true");
    }

}

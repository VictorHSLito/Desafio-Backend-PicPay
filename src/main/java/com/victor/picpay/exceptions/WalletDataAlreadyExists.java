package com.victor.picpay.exceptions;

public class WalletDataAlreadyExists extends RuntimeException{

    public WalletDataAlreadyExists(String message) {
        super(message);
    }
}

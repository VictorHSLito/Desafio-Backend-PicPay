package com.victor.picpay.exceptions;

public class UserDataAlreadyExists extends RuntimeException{

    public UserDataAlreadyExists(String message) {
        super(message);
    }
}

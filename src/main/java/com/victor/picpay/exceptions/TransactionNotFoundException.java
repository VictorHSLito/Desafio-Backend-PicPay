package com.victor.picpay.exceptions;

public class TransactionNotFoundException extends RuntimeException{
    
    public TransactionNotFoundException(String message) {
        super(message);
    }
}

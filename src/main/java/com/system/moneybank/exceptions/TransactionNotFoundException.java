package com.system.moneybank.exceptions;

public class TransactionNotFoundException extends RuntimeException{
    public TransactionNotFoundException(String message){
        super(message);
    }
}

package com.system.moneybank.exceptions;

public class RestrictedAccountException extends RuntimeException{
    public RestrictedAccountException(String message){
        super(message);
    }
}
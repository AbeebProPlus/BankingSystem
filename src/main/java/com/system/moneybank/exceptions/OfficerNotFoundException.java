package com.system.moneybank.exceptions;

public class OfficerNotFoundException extends RuntimeException{
    public OfficerNotFoundException(String message){
        super(message);
    }
}
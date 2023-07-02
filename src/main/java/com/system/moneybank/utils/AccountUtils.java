package com.system.moneybank.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.time.Year;

public class AccountUtils {
    private static final String issuerIdentifier = "5045";
    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "Sorry, this user already has an account created";

    public static final String ACCOUNT_CREATION_CODE = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account was successfully created";

    public static final String ACCOUNT_NOT_FOUND_CODE = "003";
    public static final String ACCOUNT_NOT_FOUND_MESSAGE = "Customer account not found";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "Customer account found";

    public static final String ACCOUNT_CREDITED_CODE = "005";
    public static final String ACCOUNT_CREDITED_MESSAGE = "Customer account credited successfully";

    public static final String ACCOUNT_CREDIT_DECLINED_CODE = "006";
    public static final String ACCOUNT_CREDIT_DECLINED_MESSAGE = "Invalid amount";

    public static final String ACCOUNT_DEBIT_DECLINED_CODE = "007";
    public static final String ACCOUNT_DEBIT_DECLINED_MESSAGE = "Insufficient funds";

    public static final String ACCOUNT_DEBIT_CODE = "008";
    public static final String ACCOUNT_DEBIT_MESSAGE = "Debit operation is successful";

    public static final String TRANSFER_SUCCESS_CODE = "009";
    public static final String TRANSFER_SUCCESS_MESSAGE = "Transaction successful";

    public static final String TRANSACTION_FOUND_CODE = "010";
    public static final String TRANSACTION_FOUND_MESSAGE = "Transaction found";

    public static final String TRANSACTION_NOT_FOUND_CODE = "011";
    public static final String TRANSACTION_NOT_FOUND_MESSAGE = "Transaction not found or didn't occur";
    public static final String INVALID_DESTINATION_MESSAGE = "Destination account does not exist";


    public static String generateAccountNumber(){
        int min = 100000;
        int max = 999999;
        int randomNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
        String randomNum = String.valueOf(randomNumber);
        String year = String.valueOf(Year.now());
        return year + randomNum;
    }

    public static String generateCardNumber() {
        int min = 100_000_000;
        int max = 999_999_999;
        int randomNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
        String randomNum = String.valueOf(randomNumber);
        String year = String.valueOf(Year.now().getValue());
        return issuerIdentifier + year + randomNum.substring(0, 3) + "0" + randomNum.substring(3);
    }
    public static String generateCv2() {
        int min = 100;
        int max = 999;
        int randomNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
        return String.valueOf(randomNumber);
    }


}

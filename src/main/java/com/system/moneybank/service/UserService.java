package com.system.moneybank.service;

import com.system.moneybank.dtos.request.CreateAccountRequest;
import com.system.moneybank.dtos.request.CreditDebitRequest;
import com.system.moneybank.dtos.request.EnquiryRequest;
import com.system.moneybank.dtos.request.TransferRequest;
import com.system.moneybank.dtos.response.Response;

public interface UserService {
    Response createBankAccount(CreateAccountRequest request) ;
    Response checkAccountBalance(EnquiryRequest enquiryRequest);
    String checkAccountName(EnquiryRequest request);
    Response creditAccount(CreditDebitRequest request);
    Response debitAccount(CreditDebitRequest request);
    Response transfer(TransferRequest request);
}
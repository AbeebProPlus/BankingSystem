package com.system.moneybank.service;

import com.system.moneybank.dtos.request.*;
import com.system.moneybank.dtos.response.Response;
import com.system.moneybank.dtos.response.TransactionHistoryResponse;

public interface OfficerService {
    Response createBankAccount(CreateAccountRequest request) ;
    Response checkAccountBalance(EnquiryRequest enquiryRequest);
    String checkAccountName(EnquiryRequest request);
    Response creditAccount(CreditDebitRequest request);
    Response debitAccount(CreditDebitRequest request);
}

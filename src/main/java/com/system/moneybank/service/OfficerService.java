package com.system.moneybank.service;

import com.system.moneybank.dtos.request.*;
import com.system.moneybank.dtos.response.AuthResponse;
import com.system.moneybank.dtos.response.Response;
import com.system.moneybank.dtos.response.RestrictAccountResponse;
import com.system.moneybank.dtos.response.TransactionHistoryResponse;
import com.system.moneybank.models.BankingHallTransaction;

import java.util.List;

public interface OfficerService {
    AuthResponse authenticateAndGetToken(AuthRequest authRequest);
    Response createBankAccount(CreateAccountRequest request) ;
    Response checkAccountBalance(EnquiryRequest enquiryRequest);
    String checkAccountName(EnquiryRequest request);
    Response creditAccount(CreditDebitRequest request);
    Response debitAccount(CreditDebitRequest request);
    TransactionHistoryResponse getAllTransactionsDoneByCustomer(TransactionHistoryRequest request);
    List<BankingHallTransaction> viewAllBankingHallTransactions();
    RestrictAccountResponse restrictBankAccount(RestrictAccountRequest request);
}

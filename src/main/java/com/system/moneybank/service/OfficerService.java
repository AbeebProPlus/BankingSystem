package com.system.moneybank.service;

import com.system.moneybank.dtos.request.*;
import com.system.moneybank.dtos.response.*;
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
    RestrictAccountResponse activateBankAccount(ActivateAccount request);
    CardResponse createCard(RequestForCard request);
    CardResponse deActivateCard(DeactivateCard request);
}

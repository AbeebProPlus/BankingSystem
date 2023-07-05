package com.system.moneybank.service;

import com.system.moneybank.dtos.request.CreditDebitRequest;
import com.system.moneybank.dtos.request.TransactionHistoryRequest;
import com.system.moneybank.dtos.response.FoundTransactionResponse;
import com.system.moneybank.dtos.response.TransactionHistoryResponse;
import com.system.moneybank.models.*;


import java.util.List;

public interface TransactionService {
    Transaction save(Transaction transaction);
//    BankingHallTransaction saveTransaction(BankingHallTransaction bankingHallTransaction);
    FoundTransactionResponse getATransactionDetails(String id);
//    List<BankingHallTransaction> viewAllBankingHallTransactions();
    TransactionHistoryResponse getAllTransactionsDoneByCustomer(TransactionHistoryRequest request);
    Transaction createTransaction(CreditDebitRequest request, Customer creditedUser,
                                  TransactionType type, TransactionStatus status, Officer officer);
}

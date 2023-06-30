package com.system.moneybank.service;

import com.system.moneybank.dtos.response.FoundTransactionResponse;
import com.system.moneybank.models.BankingHallTransaction;
import com.system.moneybank.models.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction save(Transaction transaction);
    FoundTransactionResponse getATransactionDetails(String id);
    List<BankingHallTransaction> viewAllBankingHallTransactions();
}

package com.system.moneybank.service;

import com.system.moneybank.dtos.response.FoundTransactionResponse;
import com.system.moneybank.models.BankingHallTransaction;
import com.system.moneybank.models.Transaction;
import com.system.moneybank.repository.BankingHallTransactionRepo;

import java.util.List;

public interface TransactionService {
    Transaction save(Transaction transaction);
    BankingHallTransaction saveTransaction(BankingHallTransaction bankingHallTransaction);
    FoundTransactionResponse getATransactionDetails(String id);
    List<BankingHallTransaction> viewAllBankingHallTransactions();
}

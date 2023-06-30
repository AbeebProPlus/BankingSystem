package com.system.moneybank.service;

import com.system.moneybank.dtos.request.TransactionRequest;
import com.system.moneybank.models.Transaction;

public interface TransactionService {
    Transaction save(TransactionRequest request);
}

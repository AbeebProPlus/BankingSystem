package com.system.moneybank.service;

import com.system.moneybank.dtos.request.TransactionRequest;
import com.system.moneybank.models.Customer;
import com.system.moneybank.models.Transaction;
import com.system.moneybank.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.system.moneybank.models.TransactionStatus.SUCCESS;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepo transactionRepo;

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepo.save(transaction);
    }
}
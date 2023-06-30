package com.system.moneybank.service;

import com.system.moneybank.dtos.request.TransactionRequest;
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
    public Transaction save(TransactionRequest request) {
        Transaction newTransaction =  Transaction.builder()
                .type(request.getType())
                .amount(request.getAmount())
                .accountNumber(request.getAccountNumber())
                .status(SUCCESS)
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();
        return transactionRepo.save(newTransaction);
    }
}
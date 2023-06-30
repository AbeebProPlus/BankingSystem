package com.system.moneybank.service;


import com.system.moneybank.dtos.response.FoundTransactionResponse;
import com.system.moneybank.exceptions.TransactionNotFoundException;
import com.system.moneybank.models.Customer;
import com.system.moneybank.models.Transaction;
import com.system.moneybank.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.system.moneybank.utils.AccountUtils.*;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepo transactionRepo;

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepo.save(transaction);
    }

    @Override
    public FoundTransactionResponse getATransactionDetails(String id) {
        try {
            Transaction transaction = transactionRepo.findById(id)
                    .orElseThrow(() -> new TransactionNotFoundException(TRANSACTION_NOT_FOUND_MESSAGE));
            return FoundTransactionResponse.builder().code(TRANSACTION_FOUND_CODE).message(TRANSACTION_FOUND_MESSAGE)
                    .transaction(transaction).build();
        }catch (Exception ex){
            return FoundTransactionResponse.builder().code(TRANSACTION_NOT_FOUND_CODE).message(ex.getMessage())
                    .transaction(null).build();
        }
    }

}
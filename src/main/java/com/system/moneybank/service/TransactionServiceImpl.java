package com.system.moneybank.service;


import com.system.moneybank.dtos.request.CreditDebitRequest;
import com.system.moneybank.dtos.request.TransactionHistoryRequest;
import com.system.moneybank.dtos.response.FoundTransactionResponse;
import com.system.moneybank.dtos.response.TransactionHistoryResponse;
import com.system.moneybank.exceptions.CustomerNotFound;
import com.system.moneybank.exceptions.TransactionNotFoundException;
import com.system.moneybank.models.*;
import com.system.moneybank.repository.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.system.moneybank.utils.AccountUtils.*;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;
//    private final BankingHallTransactionRepo bankingHallTransactionRepo;
    private final UserService userService;

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepo.save(transaction);
    }

//    @Override
//    public BankingHallTransaction saveTransaction(BankingHallTransaction bankingHallTransaction) {
//        return bankingHallTransactionRepo.save(bankingHallTransaction);
//    }

    @Override
    public FoundTransactionResponse getATransactionDetails(String id) {
        try {
            Transaction transaction = transactionRepo.findById(id)
                    .orElseThrow(() -> new TransactionNotFoundException(TRANSACTION_NOT_FOUND_MESSAGE));
            return FoundTransactionResponse.builder().code(TRANSACTION_FOUND_CODE).message(TRANSACTION_FOUND_MESSAGE)
                    .transaction(transaction).build();
        } catch (Exception ex) {
            return FoundTransactionResponse.builder().code(TRANSACTION_NOT_FOUND_CODE).message(ex.getMessage())
                    .transaction(null).build();
        }
    }

//    @Override
//    public List<BankingHallTransaction> viewAllBankingHallTransactions() {
//        return bankingHallTransactionRepo.findAll();
//    }

    @Override
    public TransactionHistoryResponse getAllTransactionsDoneByCustomer(TransactionHistoryRequest request) {
        try {
            Customer customer = userService.findByAccountNumber(request.getAccountNumber());
            if (customer == null) throw new CustomerNotFound(ACCOUNT_NOT_FOUND_MESSAGE);
            if (customer.getTransactionList().size() == 0)
                return TransactionHistoryResponse.builder().code(ACCOUNT_FOUND_CODE).message("No transactions").transactionList(customer.getTransactionList()).build();
            return TransactionHistoryResponse.builder().code(ACCOUNT_FOUND_CODE).message(ACCOUNT_FOUND_MESSAGE).transactionList(customer.getTransactionList()).build();
        } catch (Exception ex) {
            return TransactionHistoryResponse.builder().code(ACCOUNT_NOT_FOUND_CODE).message(ex.getMessage()).transactionList(null).build();
        }
    }

    @Override
    public Transaction createTransaction(CreditDebitRequest request, Customer creditedUser,
                                         TransactionType type, TransactionStatus status, Officer officer) {
        return Transaction.builder()
                .amount(request.getAmount()).accountNumber(creditedUser.getAccountNumber())
                .type(type).status(status).date(LocalDate.now())
                .time(LocalTime.now()).customer(creditedUser).officer(officer).build();
    }

}
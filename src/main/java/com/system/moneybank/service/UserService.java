package com.system.moneybank.service;

import com.system.moneybank.dtos.request.*;
import com.system.moneybank.dtos.response.Response;
import com.system.moneybank.dtos.response.TransactionHistoryResponse;
import com.system.moneybank.models.Customer;
import com.system.moneybank.models.Transaction;

import java.util.List;

public interface UserService {
    boolean existsByAccountNumber(String accountNumber);
    boolean existsByEmail(String email);
    Customer findByAccountNumber(String accountNumber);
    Customer save(Customer customer);

    boolean existsByPhoneNumber(String phoneNumber);
}
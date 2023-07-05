package com.system.moneybank.service;

import com.system.moneybank.models.Customer;

public interface UserService {
    boolean existsByAccountNumber(String accountNumber);
    boolean existsByEmail(String email);
    Customer findByAccountNumber(String accountNumber);
    Customer save(Customer customer);
    boolean existsByPhoneNumber(String phoneNumber);
}
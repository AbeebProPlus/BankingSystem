package com.system.moneybank.repository;

import com.system.moneybank.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);

    boolean existsByAccountNumber(String accountNumber);

    Customer findByAccountNumber(String accountNumber);
}
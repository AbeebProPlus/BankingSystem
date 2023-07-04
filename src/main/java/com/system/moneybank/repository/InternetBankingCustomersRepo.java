package com.system.moneybank.repository;

import com.system.moneybank.models.InternetBankingCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternetBankingCustomersRepo extends JpaRepository<InternetBankingCustomer, Long> {
    InternetBankingCustomer findByAccountNumber(String accountNumber);
}

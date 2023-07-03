package com.system.moneybank.repository;

import com.system.moneybank.models.InternetBankingCustomers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternetBankingCustomersRepo extends JpaRepository<InternetBankingCustomers, Long> {
}

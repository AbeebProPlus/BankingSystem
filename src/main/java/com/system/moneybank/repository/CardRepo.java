package com.system.moneybank.repository;

import com.system.moneybank.models.Card;
import com.system.moneybank.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepo extends JpaRepository<Card, Long> {
    Card findByCardNumber(String cardNumber);
    Card findByAccountNumber(String accountNumber);
    Card findByCustomerId(Long id);
}
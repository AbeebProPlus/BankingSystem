package com.system.moneybank.repository;

import com.system.moneybank.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepo extends JpaRepository<Card, Long> {
    Card findByCardNumber(String cardNumber);
    Card findByAccountNumber(String accountNumber);
}
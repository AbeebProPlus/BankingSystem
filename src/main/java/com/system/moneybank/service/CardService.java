package com.system.moneybank.service;

import com.system.moneybank.models.Card;
import com.system.moneybank.models.Customer;

public interface CardService {
    Card saveCard(Card card);
    Card findCardByNumber(String cardNumber);
    Card findCardByAccountNumber(String accountNumber);
    Card findCardByCustomerId(Long id);
}

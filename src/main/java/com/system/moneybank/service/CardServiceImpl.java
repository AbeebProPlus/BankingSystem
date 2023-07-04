package com.system.moneybank.service;

import com.system.moneybank.models.Card;
import com.system.moneybank.repository.CardRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService{
    private final CardRepo cardRepo;
    @Override
    public Card saveCard(Card card) {
        return cardRepo.save(card);
    }

    @Override
    public Card findCardByNumber(String cardNumber) {
        return cardRepo.findByCardNumber(cardNumber);
    }

    @Override
    public Card findCardByAccountNumber(String accountNumber) {
        return cardRepo.findByAccountNumber(accountNumber);
    }

    @Override
    public Card findCardByCustomerId(Long id) {
        return cardRepo.findByCustomerId(id);
    }
}

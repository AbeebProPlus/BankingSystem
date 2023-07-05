package com.system.moneybank.service;

import com.system.moneybank.dtos.request.CardDeactivationRequest;
import com.system.moneybank.dtos.request.ChangeCardPinRequest;
import com.system.moneybank.dtos.request.DeactivateCard;
import com.system.moneybank.dtos.request.RequestForCard;
import com.system.moneybank.dtos.response.CardResponse;
import com.system.moneybank.models.Card;

public interface CardService {
    Card findCardByCustomerId(Long id);
    CardResponse createCard(RequestForCard request);
    CardResponse deActivateCardByOfficer(DeactivateCard request);
    CardResponse activateCard(ChangeCardPinRequest request);
    CardResponse deActivateCard(CardDeactivationRequest request);
    CardResponse reActivateCard(DeactivateCard request);
    CardResponse changeCardPin(ChangeCardPinRequest request);
}

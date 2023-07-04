package com.system.moneybank.service;

import com.system.moneybank.dtos.request.*;
import com.system.moneybank.dtos.response.*;


public interface InternetBankingService {
    AuthResponse authenticateAndGetToken(AuthRequest authRequest);
    InternetBankingRegistrationResponse signUp(RegisterForInternetBanking request);
    Response transfer(TransferRequest request);
    TransactionHistoryResponse getAllTransactionsDoneByCustomer(TransactionHistoryRequest request);
    CardResponse changeCardPin(ChangeCardPinRequest request);
    CardResponse deActivateCard(CardDeactivationRequest request);
    Response checkAccountBalance(EnquiryRequest enquiryRequest);
}

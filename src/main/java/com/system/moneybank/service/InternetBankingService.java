package com.system.moneybank.service;

import com.system.moneybank.dtos.request.*;
import com.system.moneybank.dtos.response.CardResponse;
import com.system.moneybank.dtos.response.InternetBankingRegistrationResponse;
import com.system.moneybank.dtos.response.Response;
import com.system.moneybank.dtos.response.TransactionHistoryResponse;


public interface InternetBankingService {
    InternetBankingRegistrationResponse signUp(RegisterForInternetBanking request);
    Response transfer(TransferRequest request);
    TransactionHistoryResponse getAllTransactionsDoneByCustomer(TransactionHistoryRequest request);
    CardResponse changeCardPin(ChangeCardPinRequest request);
    CardResponse deActivateCard(DeactivateCard request);
    Response checkAccountBalance(EnquiryRequest enquiryRequest);
}

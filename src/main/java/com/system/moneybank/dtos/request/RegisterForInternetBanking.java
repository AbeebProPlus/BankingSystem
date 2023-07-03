package com.system.moneybank.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterForInternetBanking {
    private String accountNumber;
    private String lastSixDigitsOfCardNumber;
    private String cardPin;
    private String cardExpiryDate;
    private String preferredTransactionPin;
    private String userName;
    private String password;
}
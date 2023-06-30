package com.system.moneybank.dtos.response;


import com.system.moneybank.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoundTransactionResponse {
    private String code;
    private String message;
    private Transaction transaction;
}

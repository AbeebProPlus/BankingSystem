package com.system.moneybank.dtos.response;

import com.system.moneybank.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryResponse {
    private String message;
    private String code;
    private List<Transaction> transactionList;
}

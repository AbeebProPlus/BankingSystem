package com.system.moneybank.dtos.request;

import com.system.moneybank.models.TransactionStatus;
import com.system.moneybank.models.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private TransactionType type;
    private BigDecimal amount;
    private String accountNumber;
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus status;
}
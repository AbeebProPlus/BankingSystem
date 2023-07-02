package com.system.moneybank.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditDebitRequest {
    @NotEmpty(message = "Amount cannot be empty")
    @NotBlank(message = "Amount cannot be empty")
    @NotNull(message = "Amount cannot be empty")
    private BigDecimal amount;
    @NotEmpty(message = "Amount cannot be empty")
    @NotBlank(message = "Amount cannot be empty")
    @NotNull(message = "Amount cannot be empty")
    private String accountNumber;
    @NotEmpty(message = "Amount cannot be empty")
    @NotBlank(message = "Amount cannot be empty")
    @NotNull(message = "Amount cannot be empty")
    private String depositorName;
    @NotEmpty(message = "Amount cannot be empty")
    @NotBlank(message = "Amount cannot be empty")
    @NotNull(message = "Amount cannot be empty")
    private String officerId;
}

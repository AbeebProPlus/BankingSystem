package com.system.moneybank.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnquiryRequest {
    @NotNull(message = "Account number cannot be empty")
    @NotBlank(message = "Account number cannot be empty")
    @NotEmpty(message = "Account number cannot be empty")
    private String accountNumber;
}

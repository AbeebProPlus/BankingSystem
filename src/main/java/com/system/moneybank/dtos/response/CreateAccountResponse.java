package com.system.moneybank.dtos.response;

import com.system.moneybank.models.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountResponse {
    private String code;
    private String message;
    private Customer customer;
}

package com.system.moneybank.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class InternetBankingCustomer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Account number cannot be empty")
    private String accountNumber;
    @NotBlank(message = "UserName cannot be empty")
    private String userName;
    @NotBlank(message = "Password cannot be empty")
    private String password;
    @NotBlank(message = "Transaction pin cannot be empty")
    private String transactionPin;
    private String role;
}
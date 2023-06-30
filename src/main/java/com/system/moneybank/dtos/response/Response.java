package com.system.moneybank.dtos.response;


import com.system.moneybank.models.AccountDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {
    private String code;
    private String message;
    private AccountDetails accountDetails;
}

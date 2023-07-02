package com.system.moneybank.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateCardResponse {
    private String code;
    private String message;

}

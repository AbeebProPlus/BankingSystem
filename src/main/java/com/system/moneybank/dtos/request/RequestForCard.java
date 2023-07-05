package com.system.moneybank.dtos.request;

import com.system.moneybank.models.CardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestForCard {
    private String accountNumber;
    private Long officerId;
}

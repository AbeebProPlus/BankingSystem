package com.system.moneybank.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Give card a number")
    private String cardNumber;
    @NotBlank(message = "Give card an expiry month")
    private String expiryMonth;
    @NotBlank(message = "Give card an expiry year")
    private String expiryYear;
    @NotBlank(message = "Give card a name")
    private String cardName;
    @Enumerated(EnumType.STRING)
    private CardType cardType;
    @NotBlank(message = "Give card a cv2")
    private String cv2;
    @NotBlank(message = "Provide issuer name")
    private String issuingOfficerId;
    private String accountNumber;
    @ManyToOne
    private Customer customer;
    @Enumerated(EnumType.STRING)
    private CardStatus status;
}

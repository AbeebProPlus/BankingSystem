package com.system.moneybank.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Officer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Username is required")
    private String userName;

    @NotBlank
    private String password;

    @Enumerated(value = EnumType.STRING)
    private String role;

    @OneToMany(mappedBy = "officer", fetch = FetchType.LAZY)
    List<BankingHallTransaction> doneTransactions = new ArrayList<>();
}

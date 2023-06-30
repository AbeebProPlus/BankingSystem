package com.system.moneybank.models;

import jakarta.persistence.*;
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
    private String userName;
    @Enumerated(value = EnumType.STRING)
    private Role role = Role.OFFICER;
    @OneToMany(mappedBy = "officer", fetch = FetchType.LAZY)
    List<BankingHallTransaction> doneTransactions = new ArrayList<>();
}
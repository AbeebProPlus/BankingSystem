package com.system.moneybank.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phoneNumber;
    private String secondPhoneNumber;
    private String gender;
    private String address;
    private String accountNumber;
    private BigDecimal accountBalance;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Transaction> transactionList = new ArrayList<>();
}
package com.system.moneybank.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static jakarta.persistence.GenerationType.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;
    @Enumerated(value = EnumType.STRING)
    private TransactionType type;
    private BigDecimal amount;
    private String accountNumber;
    private LocalDate date;
    private LocalTime time;
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus status;
    @ManyToOne
    @JoinColumn(name = "cutomer_id")
    private Customer customer;
}

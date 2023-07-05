//package com.system.moneybank.models;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//import static com.system.moneybank.models.TransactionStatus.PENDING;
//import static jakarta.persistence.GenerationType.UUID;
//
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//
//public class BankingHallTransaction {
//    @Id
//    @GeneratedValue(strategy = UUID)
//    private String id;
//    @Enumerated(value = EnumType.STRING)
//    private TransactionType type;
//    private BigDecimal amount;
//    private String accountNumber;
//    private LocalDate date;
//    private LocalTime time;
//    @Enumerated(value = EnumType.STRING)
//    private TransactionStatus status = PENDING;
//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "officer_id")
//    private Officer officer;
//    private String processedBy;
//}

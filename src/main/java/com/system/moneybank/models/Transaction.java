package com.system.moneybank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", message = "Amount must be greater than or equal to 0")
    private BigDecimal amount;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Time is required")
    private LocalTime time;

    @Enumerated(value = EnumType.STRING)
    private TransactionStatus status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cutomer_id")
    private Customer customer;
    @NotBlank(message = "Officer name is required")
    private String processedBy;
}

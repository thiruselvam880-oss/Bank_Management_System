package org.example.Bank_Management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.Bank_Management.enums.TransactionStatus;
import org.example.Bank_Management.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
    public class Transaction {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long transactionId;

        @Column(unique = true, nullable = false)
        private String transactionReference;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TransactionType transactionType;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TransactionStatus transactionStatus;

        @Column(nullable = false)
        private BigDecimal amount;

        @Column(nullable = false)
        private BigDecimal availableBalanceAfterTransaction;

        private String description;

        private String fromAccountNumber;

        private String toAccountNumber;

        private LocalDateTime transactionDateTime;
        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "account_id", nullable = false)
        private Account account;
    }


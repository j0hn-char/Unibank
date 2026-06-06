package com.unibank.bankingSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal principalAmount;

    @Column(nullable = false)
    private BigDecimal remainingAmount;

    @Column(nullable = false)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private int termMonths;

    @Column(nullable = false)
    private BigDecimal monthlyPayment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Column(nullable = false)
    private String purpose;

    @Column(nullable = false)
    private LocalDateTime appliedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User borrower;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
    }
}

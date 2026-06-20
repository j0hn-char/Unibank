package com.unibank.bankingSystem.dto;

import com.unibank.bankingSystem.model.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class LoanResponse {
    private Long id;
    private BigDecimal principalAmount;
    private BigDecimal remainingBalance;
    private BigDecimal interestRate;
    private int termMonths;
    private BigDecimal monthlyPayment;
    private LoanStatus status;
    private String purpose;
    private LocalDateTime appliedAt;
}

package com.unibank.bankingSystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanRepaymentRequest {
    private BigDecimal amount;
}
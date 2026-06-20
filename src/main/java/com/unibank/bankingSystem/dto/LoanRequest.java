package com.unibank.bankingSystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class LoanRequest {
    private BigDecimal amount;
    private Integer termMonths;
    private String purpose;
    private Long accountId;
}

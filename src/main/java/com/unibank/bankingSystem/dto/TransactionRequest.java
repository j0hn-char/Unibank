package com.unibank.bankingSystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequest {
    private Long accountId;
    private BigDecimal amount;
    private String description;
}

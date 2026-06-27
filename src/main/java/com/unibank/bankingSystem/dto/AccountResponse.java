package com.unibank.bankingSystem.dto;

import com.unibank.bankingSystem.model.AccountStatus;
import com.unibank.bankingSystem.model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private String nickname;
    private AccountType type;
    private BigDecimal balance;
    private AccountStatus status;
}

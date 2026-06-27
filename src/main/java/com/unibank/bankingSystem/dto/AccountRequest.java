package com.unibank.bankingSystem.dto;

import com.unibank.bankingSystem.model.AccountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {
    private AccountType type;
    private String nickname;
}

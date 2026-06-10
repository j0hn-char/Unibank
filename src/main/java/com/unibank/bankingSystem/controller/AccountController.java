package com.unibank.bankingSystem.controller;

import com.unibank.bankingSystem.dto.AccountRequest;
import com.unibank.bankingSystem.dto.AccountResponse;
import com.unibank.bankingSystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public AccountResponse openAccount(@RequestBody AccountRequest request) {
        return accountService.openAccount(request);
    }

    @GetMapping
    public List<AccountResponse> getUserAccounts() {
        return accountService.getUserAccounts();
    }

    @GetMapping("/{id}")
    public AccountResponse getUserAccounts(@PathVariable Long id) {
        return accountService.getUserAccount(id);
    }

    @DeleteMapping("/{id}")
    public  AccountResponse closeAccount(@PathVariable Long id) {
        return accountService.closeAccount(id);
    }
}

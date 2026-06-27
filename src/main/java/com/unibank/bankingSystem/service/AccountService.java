package com.unibank.bankingSystem.service;

import com.unibank.bankingSystem.dto.AccountRequest;
import com.unibank.bankingSystem.dto.AccountResponse;
import com.unibank.bankingSystem.exception.BadRequestException;
import com.unibank.bankingSystem.exception.ResourceNotFoundException;
import com.unibank.bankingSystem.exception.UnauthorizedException;
import com.unibank.bankingSystem.model.Account;
import com.unibank.bankingSystem.model.AccountStatus;
import com.unibank.bankingSystem.model.User;
import com.unibank.bankingSystem.repository.AccountRepository;
import com.unibank.bankingSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountResponse openAccount(AccountRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        String accountNumber;
        do {
            accountNumber = "UNI" + System.currentTimeMillis() + (int)(Math.random() * 1000);
        } while (accountRepository.existsByAccountNumber(accountNumber));

        String accountNickname = request.getNickname();
        if(accountNickname == null || accountNickname.isBlank()) {
            accountNickname = request.getType().toString() + " ••" + accountNumber.substring(accountNumber.length() - 4);
        }

        Account account = new Account();
        account.setOwner(user);
        account.setAccountNumber(accountNumber);
        account.setNickname(accountNickname);
        account.setBalance(BigDecimal.ZERO);
        account.setType(request.getType());
        account.setStatus(AccountStatus.ACTIVE);

        accountRepository.save(account);

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getNickname(),
                account.getType(),
                account.getBalance(),
                account.getStatus()
        );
    }

    public AccountResponse closeAccount(Long accountId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException("Account not found")
        );

        if (!account.getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("You do not own this account");
        }

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new BadRequestException("Account balance must be zero before closing");
        }

        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getNickname(),
                account.getType(),
                account.getBalance(),
                account.getStatus()
        );
    }

    public List<AccountResponse> getUserAccounts() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        return accountRepository.findByOwner(user)
                .stream()
                .map(account -> new AccountResponse(
                        account.getId(),
                        account.getAccountNumber(),
                        account.getNickname(),
                        account.getType(),
                        account.getBalance(),
                        account.getStatus()
                ))
                .toList();
    }

    public AccountResponse getUserAccount(Long accountId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found")
        );

        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException("Account not found")
        );

        if (!account.getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedException("You do not own this account");
        }

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getNickname(),
                account.getType(),
                account.getBalance(),
                account.getStatus()
        );
    }
}

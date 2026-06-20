package com.unibank.bankingSystem.service;

import com.unibank.bankingSystem.dto.TransactionRequest;
import com.unibank.bankingSystem.dto.TransactionResponse;
import com.unibank.bankingSystem.dto.TransferRequest;
import com.unibank.bankingSystem.model.Account;
import com.unibank.bankingSystem.model.TransType;
import com.unibank.bankingSystem.model.Transaction;
import com.unibank.bankingSystem.model.User;
import com.unibank.bankingSystem.repository.AccountRepository;
import com.unibank.bankingSystem.repository.TransactionRepository;
import com.unibank.bankingSystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public TransactionResponse deposit(TransactionRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Account account = accountRepository.findById(request.getAccountId()).orElseThrow();
        if (!account.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        account.setBalance(account.getBalance().add(request.getAmount()));

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setBalanceAfter(account.getBalance());
        transaction.setType(TransType.DEPOSIT);
        transaction.setDescription(request.getDescription());

        accountRepository.save(account);
        transactionRepository.save(transaction);

        return new TransactionResponse(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getBalanceAfter(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }

    public TransactionResponse withdraw(TransactionRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Account account = accountRepository.findById(request.getAccountId()).orElseThrow();
        if (!account.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if(account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setBalanceAfter(account.getBalance());
        transaction.setType(TransType.WITHDRAWAL);
        transaction.setDescription(request.getDescription());

        accountRepository.save(account);
        transactionRepository.save(transaction);

        return new TransactionResponse(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getBalanceAfter(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }


    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Account fromAccount = accountRepository.findById(request.getFromAccountId()).orElseThrow();
        if (!fromAccount.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if(fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        Account toAccount = accountRepository.findById(request.getToAccountId()).orElseThrow();

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        Transaction outTransaction = new Transaction();
        outTransaction.setAccount(fromAccount);
        outTransaction.setAmount(request.getAmount());
        outTransaction.setBalanceAfter(fromAccount.getBalance());
        outTransaction.setType(TransType.TRANSFER_OUT);
        outTransaction.setDescription(request.getDescription());

        Transaction inTransaction = new Transaction();
        inTransaction.setAccount(toAccount);
        inTransaction.setAmount(request.getAmount());
        inTransaction.setBalanceAfter(toAccount.getBalance());
        inTransaction.setType(TransType.TRANSFER_IN);
        inTransaction.setDescription(request.getDescription());

        accountRepository.save(fromAccount);
        transactionRepository.save(outTransaction);

        accountRepository.save(toAccount);
        transactionRepository.save(inTransaction);

        return new TransactionResponse(
                outTransaction.getId(),
                outTransaction.getType(),
                outTransaction.getAmount(),
                outTransaction.getBalanceAfter(),
                outTransaction.getDescription(),
                outTransaction.getCreatedAt()
        );
    }

    public Page<TransactionResponse> getTransactionHistory(Long accountId, Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Account account = accountRepository.findById(accountId).orElseThrow();
        if (!account.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        Page<Transaction> transactions = transactionRepository.findByAccountOrderByCreatedAtDesc(account, pageable);

        return transactions.map(transaction -> new TransactionResponse(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getBalanceAfter(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        ));
    }
}

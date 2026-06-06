package com.unibank.bankingSystem.repository;

import com.unibank.bankingSystem.model.Account;
import com.unibank.bankingSystem.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findTransactionsByAccount(Account account, Pageable pageable);
}

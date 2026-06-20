package com.unibank.bankingSystem.repository;

import com.unibank.bankingSystem.model.Account;
import com.unibank.bankingSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    
    List<Account> findByOwner(User owner);

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);
}

package com.unibank.bankingSystem.repository;

import com.unibank.bankingSystem.model.Account;
import com.unibank.bankingSystem.model.Loan;
import com.unibank.bankingSystem.model.LoanStatus;
import com.unibank.bankingSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findLoansByAccount(Account account);

    List<Loan> findLoansByBorrower(User borrower);

    List<Loan> findLoansByStatus(LoanStatus status);
}

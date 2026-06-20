package com.unibank.bankingSystem.service;

import com.unibank.bankingSystem.dto.LoanRepaymentRequest;
import com.unibank.bankingSystem.dto.LoanRequest;
import com.unibank.bankingSystem.dto.LoanResponse;
import com.unibank.bankingSystem.model.*;
import com.unibank.bankingSystem.repository.AccountRepository;
import com.unibank.bankingSystem.repository.LoanRepository;
import com.unibank.bankingSystem.repository.TransactionRepository;
import com.unibank.bankingSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final LoanRepository loanRepository;
    private final TransactionRepository transactionRepository;

    public LoanResponse applyForLoan(LoanRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Account account = accountRepository.findById(request.getAccountId()).orElseThrow();
        if(!account.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        BigDecimal interestRate = BigDecimal.valueOf(5.0);

        double monthlyRate = interestRate.doubleValue() / 100 / 12;
        double r_pow_n = Math.pow(1 + monthlyRate, request.getTermMonths());
        double monthlyPayment = request.getAmount().doubleValue() * (monthlyRate * r_pow_n) / (r_pow_n - 1);

        Loan loan = new Loan();
        loan.setPrincipalAmount(request.getAmount());
        loan.setRemainingAmount(request.getAmount());
        loan.setInterestRate(interestRate);
        loan.setTermMonths(request.getTermMonths());
        loan.setMonthlyPayment(BigDecimal.valueOf(monthlyPayment).setScale(2, RoundingMode.HALF_UP));
        loan.setStatus(LoanStatus.PENDING);
        loan.setPurpose(request.getPurpose());
        loan.setBorrower(user);
        loan.setAccount(account);

        loanRepository.save(loan);

        return new LoanResponse(
                loan.getId(),
                loan.getPrincipalAmount(),
                loan.getRemainingAmount(),
                loan.getInterestRate(),
                loan.getTermMonths(),
                loan.getMonthlyPayment(),
                loan.getStatus(),
                loan.getPurpose(),
                loan.getAppliedAt()
        );

    }

    public LoanResponse approveLoan(Long loanId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        if(user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Not admin");
        }

        Loan loan = loanRepository.findById(loanId).orElseThrow();

        if(loan.getStatus() != LoanStatus.PENDING) {
            throw new RuntimeException("Can't approve loan");
        }

        loan.setStatus(LoanStatus.ACTIVE);

        Account account = loan.getAccount();
        account.setBalance(account.getBalance().add(loan.getPrincipalAmount()));

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(loan.getPrincipalAmount());
        transaction.setBalanceAfter(account.getBalance());
        transaction.setType(TransType.LOAN_DISBURSEMENT);

        accountRepository.save(account);
        transactionRepository.save(transaction);
        loanRepository.save(loan);

        return new LoanResponse(
                loan.getId(),
                loan.getPrincipalAmount(),
                loan.getRemainingAmount(),
                loan.getInterestRate(),
                loan.getTermMonths(),
                loan.getMonthlyPayment(),
                loan.getStatus(),
                loan.getPurpose(),
                loan.getAppliedAt()
        );
    }

    public LoanResponse rejectLoan(Long loanId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        if(user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Not admin");
        }

        Loan loan = loanRepository.findById(loanId).orElseThrow();

        if(loan.getStatus() != LoanStatus.PENDING) {
            throw new RuntimeException("Can't reject loan");
        }

        loan.setStatus(LoanStatus.REJECTED);

        loanRepository.save(loan);

        return new LoanResponse(
                loan.getId(),
                loan.getPrincipalAmount(),
                loan.getRemainingAmount(),
                loan.getInterestRate(),
                loan.getTermMonths(),
                loan.getMonthlyPayment(),
                loan.getStatus(),
                loan.getPurpose(),
                loan.getAppliedAt()
        );
    }

    public LoanResponse repayLoan(Long loanId, LoanRepaymentRequest request) {
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        Account account = loan.getAccount();

        if(loan.getStatus() == LoanStatus.PAID_OFF) {
            throw new RuntimeException("Loan already paid off");
        }

        if(account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        loan.setRemainingAmount(loan.getRemainingAmount().subtract(request.getAmount()));
        if(loan.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0) {
            loan.setStatus(LoanStatus.PAID_OFF);
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setBalanceAfter(account.getBalance());
        transaction.setType(TransType.LOAN_REPAYMENT);

        accountRepository.save(account);
        transactionRepository.save(transaction);
        loanRepository.save(loan);

        return new LoanResponse(
                loan.getId(),
                loan.getPrincipalAmount(),
                loan.getRemainingAmount(),
                loan.getInterestRate(),
                loan.getTermMonths(),
                loan.getMonthlyPayment(),
                loan.getStatus(),
                loan.getPurpose(),
                loan.getAppliedAt()
        );
    }

    public List<LoanResponse> getUserLoans() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        List<Loan> loans = loanRepository.findByBorrower(user);

        return loans.stream().map(loan ->
                new LoanResponse(
                        loan.getId(),
                        loan.getPrincipalAmount(),
                        loan.getRemainingAmount(),
                        loan.getInterestRate(),
                        loan.getTermMonths(),
                        loan.getMonthlyPayment(),
                        loan.getStatus(),
                        loan.getPurpose(),
                        loan.getAppliedAt()
                )
        ).toList();
    }
}

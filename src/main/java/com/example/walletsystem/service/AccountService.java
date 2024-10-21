package com.example.walletsystem.service;

import com.example.walletsystem.Enum.TransactionType;
import com.example.walletsystem.exception.AccountNotFoundException;
import com.example.walletsystem.exception.ExceedTransactionLimitException;
import com.example.walletsystem.model.Account;
import com.example.walletsystem.model.Transaction;
import com.example.walletsystem.repository.AccountRepository;
import com.example.walletsystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Transactional
    public Account deposit(Long accountId, BigDecimal amount) throws AccountNotFoundException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        account.setBalance(account.getBalance().add(amount));
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transactionRepository.save(transaction);
        return accountRepository.save(account);
    }
    @Transactional(rollbackFor = { ExceedTransactionLimitException.class }) // Rollback on specific exception
    public Account withdraw(Long accountId, BigDecimal amount) throws AccountNotFoundException, ExceedTransactionLimitException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        if (amount.compareTo(new BigDecimal("100000")) > 0) {
            throw new ExceedTransactionLimitException("Per transaction withdrawal limit exceeded. Max is 100,000 Rials.");
        }
        BigDecimal dailyWithdrawals = transactionRepository.sumWithdrawalsForToday(accountId, LocalDate.now());
        if (dailyWithdrawals == null) {
            dailyWithdrawals = BigDecimal.ZERO;
        }
        if (dailyWithdrawals.add(amount).compareTo(new BigDecimal("10000000")) > 0) {
            throw new ExceedTransactionLimitException("Daily withdrawal limit exceeded. Max is 10 million Rials per day.");
        }
        account.setBalance(account.getBalance().subtract(amount));
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transactionRepository.save(transaction);
        return accountRepository.save(account);
    }
    public Account getAccountById(Long accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
    }
}

package com.example.walletsystem.service;

import com.example.walletsystem.Enum.TransactionType;
import com.example.walletsystem.exception.ExceedTransactionLimitException;
import com.example.walletsystem.model.Account;
import com.example.walletsystem.model.Transaction;
import com.example.walletsystem.repository.AccountRepository;
import com.example.walletsystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    public Account deposit(Long accountId, BigDecimal amount) throws AccountNotFoundException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transactionRepository.save(transaction);
        return account;
    }
    public Account withdraw(Long accountId, BigDecimal amount) throws AccountNotFoundException, ExceedTransactionLimitException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        BigDecimal currentWithdrawals = transactionRepository.sumWithdrawalsForToday(accountId, LocalDate.now());
        if (currentWithdrawals.add(amount).compareTo(new BigDecimal("1000000")) > 0) {
            throw new ExceedTransactionLimitException("Withdrawal limit exceeded");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transactionRepository.save(transaction);
        return account;
    }
    private Transaction createTransaction(Account account, BigDecimal amount, TransactionType type) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionType(type);
        return transaction;
    }
    private void validateWithdrawal(Account account, BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("100000")) > 0) {
            throw new ExceedTransactionLimitException("Withdrawal limit exceeded");
        }
        BigDecimal dailyWithdrawals = transactionRepository.sumWithdrawalsForToday(account.getAccountId(), LocalDate.now());
        if (dailyWithdrawals.add(amount).compareTo(new BigDecimal("10000000")) > 0) {
            throw new ExceedTransactionLimitException("Daily withdrawal limit exceeded");
        }
    }
}

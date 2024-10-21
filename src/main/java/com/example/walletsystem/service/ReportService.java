package com.example.walletsystem.service;

import com.example.walletsystem.exception.AccountNotFoundException;
import com.example.walletsystem.model.Account;
import com.example.walletsystem.model.Transaction;
import com.example.walletsystem.repository.AccountRepository;
import com.example.walletsystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    public String generateMonthlyReport(Long accountId, int month, int year) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        List<Transaction> transactions = transactionRepository
                .findTransactionsByAccountAndMonthAndYear(accountId, month, year);
        StringBuilder report = new StringBuilder();
        report.append("Monthly Report for Account: ").append(account.getAccountNumber()).append("\n");
        report.append("Month: ").append(month).append("/").append(year).append("\n");
        report.append("Balance: ").append(account.getBalance()).append("\n");
        report.append("Transactions:\n");
        for (Transaction transaction : transactions) {
            report.append(transaction.getTransactionDate())
                    .append(" - ").append(transaction.getTransactionType())
                    .append(" - ").append(transaction.getAmount()).append("\n");
        }
        return report.toString();
    }
}

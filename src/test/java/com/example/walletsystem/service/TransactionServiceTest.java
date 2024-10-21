package com.example.walletsystem.service;

import com.example.walletsystem.exception.ExceedTransactionLimitException;
import com.example.walletsystem.model.Account;
import com.example.walletsystem.model.Transaction;
import com.example.walletsystem.repository.AccountRepository;
import com.example.walletsystem.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private TransactionService transactionService;
    private Account account;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setAccountId(1L);
        account.setBalance(new BigDecimal("50000"));
    }
    @Test
    void deposit_Success() throws AccountNotFoundException {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        Account result = transactionService.deposit(1L, new BigDecimal("10000"));
        assertNotNull(result, "Result should not be null"); // Add null check
        assertEquals(new BigDecimal("60000"), result.getBalance(), "Balance after deposit should be 60000");
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(account);
    }
    @Test
    void withdraw_Success() throws AccountNotFoundException {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.sumWithdrawalsForToday(anyLong(), any(LocalDate.class))).thenReturn(new BigDecimal("20000"));
        Account result = transactionService.withdraw(1L, new BigDecimal("10000"));
        assertNotNull(result, "Result should not be null"); // Add null check
        assertEquals(new BigDecimal("40000"), result.getBalance(), "Balance after withdrawal should be 40000");
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(account);
    }
    @Test
    void withdraw_ExceedTransactionLimit() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.sumWithdrawalsForToday(anyLong(), any(LocalDate.class))).thenReturn(new BigDecimal("9500000"));
        ExceedTransactionLimitException exception = assertThrows(ExceedTransactionLimitException.class, () ->
                transactionService.withdraw(1L, new BigDecimal("1000000"))
        );
        assertEquals("Withdrawal limit exceeded", exception.getMessage(), "Exception message should match the expected message");
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(accountRepository, never()).save(account);
    }
}

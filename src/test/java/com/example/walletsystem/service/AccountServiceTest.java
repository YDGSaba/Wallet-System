package com.example.walletsystem.service;

import com.example.walletsystem.model.Account;
import com.example.walletsystem.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountService accountService;
    private Account account;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setAccountId(1L);
        account.setBalance(new BigDecimal("50000"));
    }
    @Test
    void depositSuccess() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.deposit(1L, new BigDecimal("5000"));

        assertEquals(new BigDecimal("55000"), account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }
    @Test
    void withdrawSuccess() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.withdraw(1L, new BigDecimal("10000"));

        assertEquals(new BigDecimal("40000"), account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }
}

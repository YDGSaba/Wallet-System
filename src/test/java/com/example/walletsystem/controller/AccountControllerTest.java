package com.example.walletsystem.controller;

import com.example.walletsystem.config.TestSecurityConfig;
import com.example.walletsystem.service.AccountService;
import com.example.walletsystem.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private AccountService accountService;
    private Account account;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account();
        account.setAccountId(1L);
        account.setBalance(new BigDecimal("5000"));
    }

    @Test
    public void depositTest() throws Exception {
        when(accountService.deposit(eq(1L), any(BigDecimal.class))).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts/1/deposit")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER"))  // Simulate an authenticated user
                        .with(csrf())  // Add CSRF token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 5000}"))
                .andExpect(status().isOk());
    }

    @Test
    public void withdrawTest() throws Exception {
        when(accountService.withdraw(eq(1L), any(BigDecimal.class))).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts/1/withdraw")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER"))  // Simulate an authenticated user
                        .with(csrf())  // Add CSRF token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 1000}"))
                .andExpect(status().isOk());
    }

    @Test
    public void depositUnauthorizedTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 5000}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void withdrawUnauthorizedTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 1000}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void forbiddenAccessToOtherAccountTest() throws Exception {
        account.setAccountId(2L);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts/1/deposit")
                        .with(SecurityMockMvcRequestPostProcessors.user("differentUser").roles("USER"))  // Simulate a different authenticated user
                        .with(csrf())  // Add CSRF token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 5000}"))
                .andExpect(status().isForbidden());
    }
}

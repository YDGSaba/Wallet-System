package com.example.walletsystem.repository;

import com.example.walletsystem.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@EnableJpaRepositories
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.account.accountId = :accountId AND t.transactionType = 'WITHDRAWAL' AND t.transactionDate >= :date")
    BigDecimal sumWithdrawalsForToday(@Param("accountId") Long accountId, @Param("date") LocalDate date);

    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId AND MONTH(t.transactionDate) = :month AND YEAR(t.transactionDate) = :year")
    List<Transaction> findTransactionsByAccountAndMonthAndYear(@Param("accountId") Long accountId, @Param("month") int month, @Param("year") int year);
}
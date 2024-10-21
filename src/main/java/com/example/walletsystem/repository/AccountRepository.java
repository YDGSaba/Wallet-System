package com.example.walletsystem.repository;

import com.example.walletsystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface AccountRepository extends JpaRepository<Account, Long> { }



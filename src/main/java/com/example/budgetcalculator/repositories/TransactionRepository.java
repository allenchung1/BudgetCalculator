package com.example.budgetcalculator.repositories;

import com.example.budgetcalculator.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

package com.example.budgetcalculator.repositories;

import com.example.budgetcalculator.entities.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long> {
}

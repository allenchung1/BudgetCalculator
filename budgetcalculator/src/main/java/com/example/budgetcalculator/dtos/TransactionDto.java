package com.example.budgetcalculator.dtos;

import com.example.budgetcalculator.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class TransactionDto {
    private Long id;
    private String name;
    private BigDecimal amount;
    private LocalDate date;
    private TransactionType type;
    private Long userId;
}

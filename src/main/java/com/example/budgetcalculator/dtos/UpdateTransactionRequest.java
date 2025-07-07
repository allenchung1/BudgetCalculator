package com.example.budgetcalculator.dtos;

import com.example.budgetcalculator.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateTransactionRequest {
    private String name;
    private BigDecimal amount;
    private LocalDate date;
    private TransactionType type;
}

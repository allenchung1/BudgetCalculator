package com.example.budgetcalculator.dtos;

import com.example.budgetcalculator.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateTransactionRequest {
    @NotBlank private String name;
    @NotNull @Positive private BigDecimal amount;
    @NotNull private Long userId;
    @NotNull private LocalDate date;
    @NotNull private TransactionType type;
}

package com.example.budgetcalculator.dtos;

import com.example.budgetcalculator.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateTransactionRequest {
    @NotBlank @Size(max = 50) private String name;
    @NotNull @Positive private BigDecimal amount;
    @NotNull private LocalDate date;
    @NotNull private TransactionType type;
}

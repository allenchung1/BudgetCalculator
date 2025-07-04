package com.example.budgetcalculator.controllers;
import com.example.budgetcalculator.dtos.TransactionDto;
import com.example.budgetcalculator.entities.Transaction;
import com.example.budgetcalculator.enums.TransactionType;
import com.example.budgetcalculator.mappers.TransactionMapper;
import com.example.budgetcalculator.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @GetMapping
    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream().map(transactionMapper::toDto).toList();
    }

    @GetMapping("{id}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long id) {
        var transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactionMapper.toDto(transaction));
    }

    @GetMapping("/user/{userId}")
    public List<TransactionDto> getUserTransactions(
            @PathVariable Long userId,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false, defaultValue = "date") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDir,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
            ) {
        if (!Set.of("date", "amount").contains(sortBy)) {
            sortBy = "date";
        }
        if (!Set.of("asc", "desc").contains(sortDir)) {
            sortDir = "desc";
        }
        Sort sort = sortDir.equals("asc") ? Sort.by(Sort.Direction.ASC, sortBy) : Sort.by(Sort.Direction.DESC, sortBy);
        List<Transaction> transactions;
        if (startDate != null && endDate != null) {
            transactions = (type == null)
                    ? transactionRepository.findByUserIdAndDateBetween(userId, startDate, endDate, sort)
                    : transactionRepository.findByUserIdAndTypeAndDateBetween(userId, type, startDate, endDate, sort);
        } else {
            transactions = (type == null)
                    ? transactionRepository.findByUserId(userId, sort)
                    : transactionRepository.findByUserIdAndType(userId, type, sort);
        }
        return transactions.stream().map(transactionMapper::toDto).toList();
    }
}

package com.example.budgetcalculator.controllers;
import com.example.budgetcalculator.dtos.TransactionDto;
import com.example.budgetcalculator.mappers.TransactionMapper;
import com.example.budgetcalculator.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}

package com.example.budgetcalculator.controllers;
import com.example.budgetcalculator.dtos.CreateTransactionRequest;
import com.example.budgetcalculator.dtos.TransactionDto;
import com.example.budgetcalculator.dtos.UpdateTransactionRequest;
import com.example.budgetcalculator.enums.TransactionType;
import com.example.budgetcalculator.services.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public List<TransactionDto> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long id) {
        return transactionService.getTransaction(id);
    }

    @GetMapping("/users/{userId}")
    public Page<TransactionDto> getUserTransactions(
            @PathVariable Long userId,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false, defaultValue = "date") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDir,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        return transactionService.getUserTransactions(userId, type, sortBy, sortDir, startDate, endDate, page, limit);
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody @Valid CreateTransactionRequest request, UriComponentsBuilder uriComponentsBuilder) {
        return transactionService.createTransaction(request, uriComponentsBuilder);
    }

    @PutMapping("/{id}/users/{userId}")
    public ResponseEntity<TransactionDto> updateTransaction(@PathVariable(name = "id") Long id, @PathVariable(name = "userId") Long userId, @RequestBody @Valid UpdateTransactionRequest request) {
        return transactionService.updateTransaction(id, userId, request);
    }

    @DeleteMapping("/{id}/users/{userId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable(name = "id") Long id, @PathVariable(name = "userId") Long userId) {
        return transactionService.deleteTransaction(id, userId);
    }
}

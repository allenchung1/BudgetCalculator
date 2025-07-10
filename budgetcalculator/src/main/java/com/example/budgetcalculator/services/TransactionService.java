package com.example.budgetcalculator.services;

import com.example.budgetcalculator.dtos.CreateTransactionRequest;
import com.example.budgetcalculator.dtos.TransactionDto;
import com.example.budgetcalculator.dtos.UpdateTransactionRequest;
import com.example.budgetcalculator.entities.Transaction;
import com.example.budgetcalculator.enums.TransactionType;
import com.example.budgetcalculator.mappers.TransactionMapper;
import com.example.budgetcalculator.repositories.TransactionRepository;
import com.example.budgetcalculator.repositories.UserRepository;
import com.example.budgetcalculator.specifications.TransactionSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream().map(transactionMapper::toDto).toList();
    }

    public ResponseEntity<TransactionDto> getTransaction(Long id) {
        var transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactionMapper.toDto(transaction));
    }

    public Page<TransactionDto> getUserTransactions(
            Long userId,
            TransactionType type,
            String sortBy,
            String sortDir,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int limit
    ) {
        Specification<Transaction> spec = TransactionSpecifications.hasUserId(userId);
        if (type != null) {
            spec = spec.and(TransactionSpecifications.hasType(type));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and(TransactionSpecifications.dateBetween(startDate, endDate));
        }

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, limit, sort);

        Page<Transaction> transactionsPage = transactionRepository.findAll(spec, pageable);

        return transactionsPage.map(transactionMapper::toDto);
    }

    public ResponseEntity<TransactionDto> createTransaction(CreateTransactionRequest request, UriComponentsBuilder uriComponentsBuilder) {
        var user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var transaction = transactionMapper.toEntity(request);
        transaction.setUser(user);
        transactionRepository.save(transaction);

        var transactionDto = transactionMapper.toDto(transaction);
        var uri = uriComponentsBuilder.path("/transactions/{id}").buildAndExpand(transactionDto.getId()).toUri();

        return ResponseEntity.created(uri).body(transactionDto);
    }

    public ResponseEntity<TransactionDto> updateTransaction(Long id, Long userId, UpdateTransactionRequest request) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }

        if (!transaction.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        transactionMapper.update(request, transaction);
        transactionRepository.save(transaction);

        return ResponseEntity.ok(transactionMapper.toDto(transaction));
    }

    public ResponseEntity<Void> deleteTransaction(Long id, Long userId) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }

        if (!transaction.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        transactionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public BigDecimal calculateTotalIncome(Long userId, LocalDate startDate, LocalDate endDate) {
        var user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (startDate == null) startDate = LocalDate.of(2025, 1, 1);
        if (endDate == null) endDate = LocalDate.now();
        return transactionRepository.sumByUserIdAndTypeAndDateRange(userId, TransactionType.INCOME, startDate, endDate);
    }

    public BigDecimal calculateTotalExpense(Long userId, LocalDate startDate, LocalDate endDate) {
        var user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (startDate == null) startDate = LocalDate.of(2025, 1, 1);
        if (endDate == null) endDate = LocalDate.now();
        return transactionRepository.sumByUserIdAndTypeAndDateRange(userId, TransactionType.EXPENSE, startDate, endDate);
    }

    public BigDecimal calculateNetBalance(Long userId, LocalDate startDate, LocalDate endDate) {
        var user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (startDate == null) startDate = LocalDate.of(2025, 1, 1);
        if (endDate == null) endDate = LocalDate.now();
        BigDecimal income = calculateTotalIncome(userId, startDate, endDate);
        BigDecimal expense = calculateTotalExpense(userId, startDate, endDate);
        return income.subtract(expense);
    }
}

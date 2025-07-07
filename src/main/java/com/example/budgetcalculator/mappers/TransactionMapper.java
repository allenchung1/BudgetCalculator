package com.example.budgetcalculator.mappers;

import com.example.budgetcalculator.dtos.CreateTransactionRequest;
import com.example.budgetcalculator.dtos.TransactionDto;
import com.example.budgetcalculator.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "user.id", target = "userId")
    TransactionDto toDto(Transaction transaction);

    Transaction toEntity(CreateTransactionRequest request);
}

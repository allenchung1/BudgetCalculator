package com.example.budgetcalculator.mappers;

import com.example.budgetcalculator.dtos.CreateUserRequest;
import com.example.budgetcalculator.dtos.UpdateUserRequest;
import com.example.budgetcalculator.dtos.UserDto;
import com.example.budgetcalculator.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(CreateUserRequest request);

    void update(UpdateUserRequest request, @MappingTarget User user);
}

package com.example.budgetcalculator.mappers;

import com.example.budgetcalculator.dtos.UserDto;
import com.example.budgetcalculator.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

}

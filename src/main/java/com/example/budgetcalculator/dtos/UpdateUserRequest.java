package com.example.budgetcalculator.dtos;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;
    private String email;
}

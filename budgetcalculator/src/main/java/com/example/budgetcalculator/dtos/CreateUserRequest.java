package com.example.budgetcalculator.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank @Size(max = 25) private String username;
    @NotBlank @Email private String email;
    @NotBlank @Size(min = 6, max = 50) private String password;
}

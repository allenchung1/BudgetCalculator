package com.example.budgetcalculator.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank @Size(min = 6, max = 50) private String oldPassword;
    @NotBlank @Size(min = 8, max = 100) private String newPassword;
}

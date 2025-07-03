package com.example.budgetcalculator.repositories;

import com.example.budgetcalculator.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

package com.example.budgetcalculator.repositories;

import com.example.budgetcalculator.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //temporary
    Optional<User> findByEmailAndPassword(String email, String password);
}

package com.example.budgetcalculator.controllers;

import com.example.budgetcalculator.dtos.UserDto;
import com.example.budgetcalculator.entities.User;
import com.example.budgetcalculator.mappers.UserMapper;
import com.example.budgetcalculator.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(required = false, defaultValue = "", name = "sort") String sort) {
        if (!Set.of("username", "email").contains(sort)) {
            sort = "username";
        }
        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto) // shorthand for .map(user -> userMapper.toDto(user))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}

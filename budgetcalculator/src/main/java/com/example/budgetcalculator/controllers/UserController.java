package com.example.budgetcalculator.controllers;

import com.example.budgetcalculator.dtos.ChangePasswordRequest;
import com.example.budgetcalculator.dtos.CreateUserRequest;
import com.example.budgetcalculator.dtos.UpdateUserRequest;
import com.example.budgetcalculator.dtos.UserDto;
import com.example.budgetcalculator.mappers.UserMapper;
import com.example.budgetcalculator.repositories.UserRepository;
import com.example.budgetcalculator.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(required = false, defaultValue = "", name = "sort") String sort) {
        return userService.getAllUsers(sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserRequest request, UriComponentsBuilder uriComponentsBuilder) {
        return userService.createUser(request, uriComponentsBuilder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") Long id, @RequestBody @Valid UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable(name = "id") Long id, @RequestBody @Valid ChangePasswordRequest request) {
        return userService.changePassword(id, request);
    }
}

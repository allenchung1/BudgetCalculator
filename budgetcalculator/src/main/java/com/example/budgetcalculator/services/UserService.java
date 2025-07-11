package com.example.budgetcalculator.services;

import com.example.budgetcalculator.dtos.*;
import com.example.budgetcalculator.entities.User;
import com.example.budgetcalculator.mappers.UserMapper;
import com.example.budgetcalculator.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers(String sort) {
        if (!Set.of("username", "email").contains(sort)) {
            sort = "username";
        }
        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto) // shorthand for .map(user -> userMapper.toDto(user))
                .toList();
    }

    public ResponseEntity<UserDto> getUser(Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    public ResponseEntity<UserDto> createUser(CreateUserRequest request, UriComponentsBuilder uriComponentsBuilder) {
        var user = userMapper.toEntity(request);
        userRepository.save(user);

        var userDto = userMapper.toDto(user);
        var uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    public ResponseEntity<UserDto> updateUser(Long id, UpdateUserRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userMapper.update(request, user);
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    public ResponseEntity<Void> deleteUser(Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> changePassword(Long id, ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!user.getPassword().equals(request.getOldPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<UserDto> login(LoginRequest request) {
        Optional<User> user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userMapper.toDto(user.get()));
    }

}

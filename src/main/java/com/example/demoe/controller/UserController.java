package com.example.demoe.controller;

import com.example.demoe.dto.UserDto;
import com.example.demoe.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Received request to create user {}", userDto.getId());
        UserDto created = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("Received request to get user with id {}", id);
        UserDto dto = userService.getUserById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        log.info("Received request to update user with id {}", id);
        UserDto dto = userService.updateUserById(id,userDto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        log.info("Received request to delete user with id {}", id);
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}

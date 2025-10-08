package com.example.demoe.service;

import com.example.demoe.dto.UserDto;


public interface UserService {

    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long id);
    UserDto updateUserById(Long id, UserDto userDto);
    void deleteUserById(Long id);
}

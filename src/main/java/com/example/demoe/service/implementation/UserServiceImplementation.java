package com.example.demoe.service.implementation;

import com.example.demoe.cache.UserCacheService;
import com.example.demoe.dto.UserDto;
import com.example.demoe.exception.EmailAlreadyExistsException;
import com.example.demoe.exception.ResourceNotFoundException;
import com.example.demoe.mapper.UserMapper;
import com.example.demoe.model.User;
import com.example.demoe.repository.UserRepository;
import com.example.demoe.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserCacheService userCacheService;


    public UserServiceImplementation(UserRepository userRepository,
                                     UserMapper userMapper,
                                     UserCacheService userCacheService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userCacheService = userCacheService;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Request for creating user {}", userDto.getId());

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exist");
        }

        User user = userMapper.toEntity(userDto);
        User saved = userRepository.save(user);
        UserDto dto = userMapper.toDto(saved);

        userCacheService.put(dto);
        return dto;
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Received request to get the user with id {}", id);
        UserDto cached = null;
        try {
            cached = userCacheService.get(id);
        } catch (Exception e) {
            log.warn("cache unavailable for id {}", id);
        }
        if (cached != null) {
            return cached;
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with suer id " + id));

        UserDto userDto = userMapper.toDto(user);

        userCacheService.put(userDto);
        return userDto;

    }

    @Override
    public UserDto updateUserById(Long id, UserDto userDto) {
        log.info("Received request to update user with id {}", id);

        if (id == null || userDto == null) {
            throw new IllegalArgumentException("Id and userDto not to be null");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id" + id));

        userMapper.updateEntityFromDto(userDto, user);

        User saveUser = userRepository.save(user);
        UserDto dto = userMapper.toDto(saveUser);

        try {
            userCacheService.update(id, dto);
        } catch (Exception e) {
            log.warn("updated user not cached with id {}: {}", id, e.getMessage());
        }
        return dto;
    }

    public void deleteUserById(Long id) {
        log.info("received request to delete the user with id {}", id);

        if (id == null) {
            throw new IllegalArgumentException("Id not found");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found"));

        userRepository.deleteById(id);

        userCacheService.delete(id);
    }
}

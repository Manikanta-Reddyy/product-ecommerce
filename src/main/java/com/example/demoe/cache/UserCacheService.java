package com.example.demoe.cache;

import com.example.demoe.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserCacheService {

    @Autowired
    private RedisTemplate<String, UserDto> userRedisTemplate;

    private static final String PREFIX_ID = "user:id:";

    public void put(UserDto userDto) {
        if (userDto == null || userDto.getId() == null) {
            log.error("Invalid userDto: {}", userDto);
            return;
        }

        try {
            userRedisTemplate.opsForHash().put(PREFIX_ID ,userDto.getId().toString(), userDto);
        } catch (Exception e) {
            log.error("UserDto not cached with id{}", userDto.getId().toString(), e);
        }
    }

    public UserDto get(Long id) {
        if (id == null) {
            log.warn("Attempted to get user with id null");
            throw new IllegalArgumentException("Id must not be null");
        }

        try {
            log.info("Checking cache for user with id {}", id);
            UserDto user =(UserDto) userRedisTemplate.opsForHash().get(PREFIX_ID, id.toString());

            if (user != null) {
                log.debug("Cache hit for user with id {}", id);
            } else {
                log.debug("Cache hit miss for id {}", id);
            }
            return user;

        } catch (Exception e) {
            log.error("UserDto not cached with id{");
            throw e;
        }

    }

    public void update(Long id, UserDto userDto) {
        if (id == null) {
            log.warn("Attempted to get update user with null id");
            throw new IllegalArgumentException("Id not found");
        }

        if (userDto == null) {
            log.warn("Attempted to update user with null  userDto");
            throw new IllegalArgumentException("UserDto not found");
        }

        try {
            log.info("updating cache for user with id {}", id);
            userRedisTemplate.opsForHash().put(PREFIX_ID, id.toString(), userDto);
        } catch (Exception e) {
            log.error("user with id {} not cached", id);
            throw e;
        }
    }

    public void delete(Long id) {
        if (id == null) {
            log.warn("Attempted to delete user with null id");
            throw new IllegalArgumentException("Invalid Id provided");
        }

        userRedisTemplate.opsForHash().delete(PREFIX_ID,id.toString());
    }
}

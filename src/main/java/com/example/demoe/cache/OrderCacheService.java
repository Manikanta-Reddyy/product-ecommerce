package com.example.demoe.cache;

import com.example.demoe.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OrderCacheService {

    private static final String PREFIX_ID = "order:id:";
    private static final String PREFIX_USER = "order:user:";

    @Autowired
    private RedisTemplate<String, OrderDto> orderRedisTemplate;

    public void put(OrderDto orderDto) {
        if (orderDto.getId() == null) {
            throw new IllegalArgumentException("id and orderDto not to be null");
        }

        orderRedisTemplate.opsForValue().set(PREFIX_ID + orderDto.getId(), orderDto);
    }

    public OrderDto get(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id not found");
        }

        return orderRedisTemplate.opsForValue().get(PREFIX_ID + id);

    }

    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id not found");
        }

        orderRedisTemplate.opsForValue().getAndDelete(PREFIX_ID + id);
    }


}

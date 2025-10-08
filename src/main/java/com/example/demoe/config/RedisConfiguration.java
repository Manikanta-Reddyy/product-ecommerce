package com.example.demoe.config;

import com.example.demoe.dto.OrderDto;
import com.example.demoe.dto.ProductDto;
import com.example.demoe.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @Bean
    public ObjectMapper redisJsonMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    @Bean
    public RedisTemplate<String, ProductDto> productDtoRedisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String,ProductDto> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        Jackson2JsonRedisSerializer<ProductDto> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(ProductDto.class);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean
    public RedisTemplate<String, UserDto> userRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, UserDto> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        Jackson2JsonRedisSerializer<UserDto> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(UserDto.class);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    @Bean
    public RedisTemplate<String, OrderDto> orderRedisTemplate(RedisConnectionFactory factory, ObjectMapper redisJsonMapper) {
        RedisTemplate<String, OrderDto> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        Jackson2JsonRedisSerializer<OrderDto> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(redisJsonMapper, OrderDto.class);


        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

}

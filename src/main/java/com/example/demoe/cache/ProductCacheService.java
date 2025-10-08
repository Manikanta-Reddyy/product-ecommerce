package com.example.demoe.cache;

import com.example.demoe.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class ProductCacheService {

    @Autowired
    private RedisTemplate<String, ProductDto> productRedisTemplate;

    private final String PREFIX_ID = "product:id:";
    private static final Duration cacheTTL = Duration.ofHours(1);

    public void put(ProductDto productDto) {

       if (productDto == null || productDto.getId() == null) {
           log.error("Invalid productDto provided to cache put: {}", productDto);
           return;
       }

       try {
           productRedisTemplate.opsForValue().set(PREFIX_ID + productDto.getId(), productDto, cacheTTL);
       } catch (Exception e) {
           log.error("productDto not cached: id={}, sku={}", productDto.getId(), productDto.getSku(), e);
       }
    }

    public ProductDto get(Long id) {
        if (id == null) {
            log.error("Invalid id provided");
            return null;
        }

        try {
            return productRedisTemplate.opsForValue().get(PREFIX_ID + id);
        } catch (Exception e) {
            log.error("product id not cached");
            return null;
        }
    }

    public void delete(Long id) {
        if (id == null) {
            log.warn("Attempted to delete product with null id");
            return;
        }

        try {
            productRedisTemplate.opsForValue().getAndDelete(PREFIX_ID + id);
            log.info("Deleted product with id {} from cache", id);
        } catch (Exception e) {
            log.error("Failed to delete product from Redis for id {}: {}", id, e.getMessage(), e);
        }
    }
}

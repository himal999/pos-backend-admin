package com.dtech.admin.service.impl;

import com.dtech.admin.service.CacheService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            log.debug("Cached value for key: {}", key);
        } catch (Exception e) {
            log.error("Error caching value for key: {}", key, e);
        }
    }

    @Override
    public void set(String key, Object value, long ttl, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl, timeUnit);
            log.debug("Cached value for key: {} with TTL: {} {}", key, ttl, timeUnit);
        } catch (Exception e) {
            log.error("Error caching value for key: {} with TTL", key, e);
        }
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                T result = objectMapper.convertValue(value, clazz);
                log.debug("Retrieved value from cache for key: {}", key);
                return Optional.of(result);
            }
            log.debug("Cache miss for key: {}", key);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error retrieving value from cache for key: {}", key, e);
            return Optional.empty();
        }
    }

    @Override
    public <T> List<T> getList(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                List<T> result = objectMapper.convertValue(value, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
                log.debug("Retrieved list from cache for key: {}", key);
                return result;
            }
            log.debug("Cache miss for list key: {}", key);
            return List.of();
        } catch (Exception e) {
            log.error("Error retrieving list from cache for key: {}", key, e);
            return List.of();
        }
    }

    @Override
    public boolean exists(String key) {
        try {
            Boolean exists = redisTemplate.hasKey(key);
            return exists != null && exists;
        } catch (Exception e) {
            log.error("Error checking existence for key: {}", key, e);
            return false;
        }
    }

    @Override
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
            log.debug("Deleted cache key: {}", key);
        } catch (Exception e) {
            log.error("Error deleting cache key: {}", key, e);
        }
    }

    @Override
    public void delete(List<String> keys) {
        try {
            redisTemplate.delete(keys);
            log.debug("Deleted {} cache keys", keys.size());
        } catch (Exception e) {
            log.error("Error deleting cache keys", e);
        }
    }

    @Override
    public void deleteByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.debug("Deleted {} cache keys matching pattern: {}", keys.size(), pattern);
            }
        } catch (Exception e) {
            log.error("Error deleting cache keys by pattern: {}", pattern, e);
        }
    }

    @Override
    public long getTtl(String key) {
        try {
            Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            return ttl != null ? ttl : -1;
        } catch (Exception e) {
            log.error("Error getting TTL for key: {}", key, e);
            return -1;
        }
    }

    @Override
    public boolean setTtl(String key, long ttl, TimeUnit timeUnit) {
        try {
            Boolean result = redisTemplate.expire(key, ttl, timeUnit);
            log.debug("Set TTL for key: {} to {} {}", key, ttl, timeUnit);
            return result != null && result;
        } catch (Exception e) {
            log.error("Error setting TTL for key: {}", key, e);
            return false;
        }
    }

    @Override
    public void clearAll() {
        try {
            redisTemplate.getConnectionFactory().getConnection().flushDb();
            log.info("Cleared all cache");
        } catch (Exception e) {
            log.error("Error clearing all cache", e);
        }
    }

    @Override
    public String getCacheStats() {
        try {
            Long dbSize = redisTemplate.getConnectionFactory().getConnection().dbSize();
            return String.format("Cache contains %d keys", dbSize != null ? dbSize : 0);
        } catch (Exception e) {
            log.error("Error getting cache stats", e);
            return "Unable to retrieve cache stats";
        }
    }
} 
package com.dtech.admin.util;

import com.dtech.admin.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheUtils {

    private final CacheService cacheService;

    /**
     * Get value from cache or compute if not present
     */
    public <T> T getOrCompute(String key, Class<T> clazz, Supplier<T> supplier) {
        return getOrCompute(key, clazz, supplier, CacheConstants.DEFAULT_TTL, TimeUnit.SECONDS);
    }

    /**
     * Get value from cache or compute if not present with custom TTL
     */
    public <T> T getOrCompute(String key, Class<T> clazz, Supplier<T> supplier, long ttl, TimeUnit timeUnit) {
        Optional<T> cachedValue = cacheService.get(key, clazz);
        if (cachedValue.isPresent()) {
            log.debug("Cache hit for key: {}", key);
            return cachedValue.get();
        }

        log.debug("Cache miss for key: {}, computing value", key);
        T computedValue = supplier.get();
        if (computedValue != null) {
            cacheService.set(key, computedValue, ttl, timeUnit);
        }
        return computedValue;
    }

    /**
     * Get list from cache or compute if not present
     */
    public <T> List<T> getListOrCompute(String key, Class<T> clazz, Supplier<List<T>> supplier) {
        return getListOrCompute(key, clazz, supplier, CacheConstants.DEFAULT_TTL, TimeUnit.SECONDS);
    }

    /**
     * Get list from cache or compute if not present with custom TTL
     */
    public <T> List<T> getListOrCompute(String key, Class<T> clazz, Supplier<List<T>> supplier, long ttl, TimeUnit timeUnit) {
        List<T> cachedValue = cacheService.getList(key, clazz);
        if (!cachedValue.isEmpty()) {
            log.debug("Cache hit for list key: {}", key);
            return cachedValue;
        }

        log.debug("Cache miss for list key: {}, computing value", key);
        List<T> computedValue = supplier.get();
        if (computedValue != null && !computedValue.isEmpty()) {
            cacheService.set(key, computedValue, ttl, timeUnit);
        }
        return computedValue != null ? computedValue : List.of();
    }

    /**
     * Invalidate cache by key
     */
    public void invalidate(String key) {
        cacheService.delete(key);
        log.debug("Invalidated cache key: {}", key);
    }

    /**
     * Invalidate cache by pattern
     */
    public void invalidateByPattern(String pattern) {
        cacheService.deleteByPattern(pattern);
        log.debug("Invalidated cache pattern: {}", pattern);
    }

    /**
     * Invalidate all cache keys for a specific entity type
     */
    public void invalidateEntity(String entityType) {
        String pattern = entityType + CacheConstants.PATTERN_WILDCARD;
        cacheService.deleteByPattern(pattern);
        log.debug("Invalidated all cache keys for entity: {}", entityType);
    }

    /**
     * Invalidate cache for a specific entity by ID
     */
    public void invalidateEntityById(String entityType, Long id) {
        String key = CacheConstants.buildKey(entityType + ":id:%d", id);
        cacheService.delete(key);
        log.debug("Invalidated cache for {} with ID: {}", entityType, id);
    }

    /**
     * Invalidate cache for a specific entity by name
     */
    public void invalidateEntityByName(String entityType, String name) {
        String key = CacheConstants.buildKey(entityType + ":name:%s", name);
        cacheService.delete(key);
        log.debug("Invalidated cache for {} with name: {}", entityType, name);
    }

    /**
     * Invalidate all cache keys for a specific entity and its related caches
     */
    public void invalidateEntityAndRelated(String entityType, String relatedPattern) {
        // Invalidate entity cache
        invalidateEntity(entityType);
        // Invalidate related caches
        cacheService.deleteByPattern(relatedPattern);
        log.debug("Invalidated cache for {} and related pattern: {}", entityType, relatedPattern);
    }

    /**
     * Set cache value with default TTL
     */
    public void set(String key, Object value) {
        cacheService.set(key, value, CacheConstants.DEFAULT_TTL, TimeUnit.SECONDS);
    }

    /**
     * Set cache value with custom TTL
     */
    public void set(String key, Object value, long ttl, TimeUnit timeUnit) {
        cacheService.set(key, value, ttl, timeUnit);
    }

    /**
     * Get cache value
     */
    public <T> Optional<T> get(String key, Class<T> clazz) {
        return cacheService.get(key, clazz);
    }

    /**
     * Get cache list
     */
    public <T> List<T> getList(String key, Class<T> clazz) {
        return cacheService.getList(key, clazz);
    }

    /**
     * Check if key exists in cache
     */
    public boolean exists(String key) {
        return cacheService.exists(key);
    }

    /**
     * Get cache statistics
     */
    public String getCacheStats() {
        return cacheService.getCacheStats();
    }

    /**
     * Clear all cache
     */
    public void clearAll() {
        cacheService.clearAll();
    }
} 
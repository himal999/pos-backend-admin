package com.dtech.admin.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface CacheService {
    
    /**
     * Store a value in cache with default TTL
     */
    void set(String key, Object value);
    
    /**
     * Store a value in cache with custom TTL
     */
    void set(String key, Object value, long ttl, TimeUnit timeUnit);
    
    /**
     * Get a value from cache
     */
    <T> Optional<T> get(String key, Class<T> clazz);
    
    /**
     * Get a list of values from cache
     */
    <T> List<T> getList(String key, Class<T> clazz);
    
    /**
     * Check if a key exists in cache
     */
    boolean exists(String key);
    
    /**
     * Delete a key from cache
     */
    void delete(String key);
    
    /**
     * Delete multiple keys from cache
     */
    void delete(List<String> keys);
    
    /**
     * Delete all keys matching a pattern
     */
    void deleteByPattern(String pattern);
    
    /**
     * Get TTL for a key
     */
    long getTtl(String key);
    
    /**
     * Set TTL for a key
     */
    boolean setTtl(String key, long ttl, TimeUnit timeUnit);
    
    /**
     * Clear all cache
     */
    void clearAll();
    
    /**
     * Get cache statistics
     */
    String getCacheStats();
} 
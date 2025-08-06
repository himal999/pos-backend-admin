package com.dtech.admin.controller;

import com.dtech.admin.service.CacheService;
import com.dtech.admin.util.CacheUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "Cache Management")
public class CacheController {

    private final CacheService cacheService;
    private final CacheUtils cacheUtils;

    @GetMapping("/stats")
    @ApiOperation("Get cache statistics")
    public ResponseEntity<Map<String, String>> getCacheStats() {
        String stats = cacheService.getCacheStats();
        return ResponseEntity.ok(Map.of("stats", stats));
    }

    @DeleteMapping("/clear")
    @ApiOperation("Clear all cache")
    public ResponseEntity<Map<String, String>> clearAllCache() {
        cacheService.clearAll();
        log.info("All cache cleared via API");
        return ResponseEntity.ok(Map.of("message", "All cache cleared successfully"));
    }

    @DeleteMapping("/key/{key}")
    @ApiOperation("Delete specific cache key")
    public ResponseEntity<Map<String, String>> deleteCacheKey(
            @ApiParam("Cache key to delete") @PathVariable String key) {
        cacheService.delete(key);
        log.info("Cache key deleted via API: {}", key);
        return ResponseEntity.ok(Map.of("message", "Cache key deleted successfully", "key", key));
    }

    @DeleteMapping("/pattern")
    @ApiOperation("Delete cache keys by pattern")
    public ResponseEntity<Map<String, String>> deleteCacheByPattern(
            @ApiParam("Pattern to match cache keys") @RequestParam String pattern) {
        cacheService.deleteByPattern(pattern);
        log.info("Cache keys deleted by pattern via API: {}", pattern);
        return ResponseEntity.ok(Map.of("message", "Cache keys deleted successfully", "pattern", pattern));
    }

    @DeleteMapping("/entity/{entityType}")
    @ApiOperation("Invalidate all cache for a specific entity type")
    public ResponseEntity<Map<String, String>> invalidateEntityCache(
            @ApiParam("Entity type to invalidate") @PathVariable String entityType) {
        cacheUtils.invalidateEntity(entityType);
        log.info("Entity cache invalidated via API: {}", entityType);
        return ResponseEntity.ok(Map.of("message", "Entity cache invalidated successfully", "entityType", entityType));
    }

    @DeleteMapping("/entity/{entityType}/id/{id}")
    @ApiOperation("Invalidate cache for a specific entity by ID")
    public ResponseEntity<Map<String, String>> invalidateEntityById(
            @ApiParam("Entity type") @PathVariable String entityType,
            @ApiParam("Entity ID") @PathVariable Long id) {
        cacheUtils.invalidateEntityById(entityType, id);
        log.info("Entity cache invalidated by ID via API: {} - {}", entityType, id);
        return ResponseEntity.ok(Map.of("message", "Entity cache invalidated successfully", "entityType", entityType, "id", id.toString()));
    }

    @DeleteMapping("/entity/{entityType}/name/{name}")
    @ApiOperation("Invalidate cache for a specific entity by name")
    public ResponseEntity<Map<String, String>> invalidateEntityByName(
            @ApiParam("Entity type") @PathVariable String entityType,
            @ApiParam("Entity name") @PathVariable String name) {
        cacheUtils.invalidateEntityByName(entityType, name);
        log.info("Entity cache invalidated by name via API: {} - {}", entityType, name);
        return ResponseEntity.ok(Map.of("message", "Entity cache invalidated successfully", "entityType", entityType, "name", name));
    }

    @GetMapping("/exists/{key}")
    @ApiOperation("Check if cache key exists")
    public ResponseEntity<Map<String, Object>> checkKeyExists(
            @ApiParam("Cache key to check") @PathVariable String key) {
        boolean exists = cacheService.exists(key);
        return ResponseEntity.ok(Map.of("key", key, "exists", exists));
    }

    @GetMapping("/ttl/{key}")
    @ApiOperation("Get TTL for a cache key")
    public ResponseEntity<Map<String, Object>> getKeyTtl(
            @ApiParam("Cache key") @PathVariable String key) {
        long ttl = cacheService.getTtl(key);
        return ResponseEntity.ok(Map.of("key", key, "ttl", ttl));
    }

    @PostMapping("/set")
    @ApiOperation("Set a cache value")
    public ResponseEntity<Map<String, String>> setCacheValue(
            @ApiParam("Cache key") @RequestParam String key,
            @ApiParam("Cache value") @RequestParam String value,
            @ApiParam("TTL in seconds (optional)") @RequestParam(required = false, defaultValue = "3600") Long ttl) {
        cacheService.set(key, value, ttl, java.util.concurrent.TimeUnit.SECONDS);
        log.info("Cache value set via API: {} with TTL: {}s", key, ttl);
        return ResponseEntity.ok(Map.of("message", "Cache value set successfully", "key", key, "ttl", ttl.toString()));
    }

    @DeleteMapping("/batch")
    @ApiOperation("Delete multiple cache keys")
    public ResponseEntity<Map<String, String>> deleteMultipleKeys(
            @ApiParam("List of cache keys to delete") @RequestBody List<String> keys) {
        cacheService.delete(keys);
        log.info("Multiple cache keys deleted via API: {}", keys);
        return ResponseEntity.ok(Map.of("message", "Cache keys deleted successfully", "count", String.valueOf(keys.size())));
    }
} 
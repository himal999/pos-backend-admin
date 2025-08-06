# Redis Caching Implementation

This document describes the Redis caching mechanism implemented in the admin application.

## Overview

The Redis caching system provides:
- High-performance data caching
- Automatic cache invalidation
- Configurable TTL (Time To Live)
- Cache management APIs
- Integration with existing services

## Components

### 1. Dependencies
Added to `pom.xml`:
```xml
<!-- Redis Dependencies -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>
```

### 2. Configuration Files

#### Application Properties (`application.properties`)
```properties
#Redis Configuration
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=
spring.redis.database=0
spring.redis.timeout=2000ms
spring.redis.jedis.pool.max-active=8
spring.redis.jedis.pool.max-wait=-1ms
spring.redis.jedis.pool.max-idle=8
spring.redis.jedis.pool.min-idle=0

#Cache Configuration
spring.cache.type=redis
spring.cache.redis.time-to-live=3600000
spring.cache.redis.cache-null-values=false
spring.cache.redis.use-key-prefix=true
```

#### Redis Configuration (`RedisConfig.java`)
- Configures Redis connection factory
- Sets up Redis template with proper serialization
- Configures cache manager with TTL settings

### 3. Core Classes

#### CacheService Interface
Defines the contract for cache operations:
- `set()` - Store values in cache
- `get()` - Retrieve values from cache
- `delete()` - Remove cache entries
- `exists()` - Check if key exists
- `clearAll()` - Clear all cache

#### CacheServiceImpl
Implementation of cache operations using Redis template.

#### CacheUtils
Utility class providing convenient methods:
- `getOrCompute()` - Get from cache or compute if not present
- `invalidateEntity()` - Invalidate all cache for an entity type
- `invalidateEntityById()` - Invalidate cache for specific entity ID

#### CacheConstants
Defines cache names, key patterns, and TTL constants.

## Usage Examples

### 1. Basic Caching in Services

```java
@Service
public class MyService {
    
    @Autowired
    private CacheUtils cacheUtils;
    
    public MyEntity getEntityById(Long id) {
        String cacheKey = CacheConstants.buildKey("myentity:id:%d", id);
        
        return cacheUtils.getOrCompute(cacheKey, MyEntity.class, () -> {
            // This lambda will only execute if cache miss
            return repository.findById(id).orElse(null);
        }, CacheConstants.MEDIUM_TTL, TimeUnit.SECONDS);
    }
}
```

### 2. Caching Lists

```java
public List<MyEntity> getAllEntities() {
    String cacheKey = CacheConstants.ALL_MY_ENTITIES;
    
    return cacheUtils.getListOrCompute(cacheKey, MyEntity.class, () -> {
        return repository.findAll();
    }, CacheConstants.LONG_TTL, TimeUnit.SECONDS);
}
```

### 3. Cache Invalidation

```java
public void updateEntity(MyEntity entity) {
    // Update in database
    repository.save(entity);
    
    // Invalidate related caches
    cacheUtils.invalidateEntityById("myentity", entity.getId());
    cacheUtils.invalidateByPattern("myentity:filter:*");
}
```

### 4. Using Spring Cache Annotations

```java
@Service
public class CachedService {
    
    @Cacheable(value = "myentity", key = "#id")
    public MyEntity findById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    @CacheEvict(value = "myentity", key = "#entity.id")
    public void update(MyEntity entity) {
        repository.save(entity);
    }
}
```

## Cache Management APIs

### Cache Statistics
```http
GET /admin/api/cache/stats
```

### Clear All Cache
```http
DELETE /admin/api/cache/clear
```

### Delete Specific Key
```http
DELETE /admin/api/cache/key/{key}
```

### Delete by Pattern
```http
DELETE /admin/api/cache/pattern?pattern=user:*
```

### Invalidate Entity Cache
```http
DELETE /admin/api/cache/entity/{entityType}
```

### Invalidate Entity by ID
```http
DELETE /admin/api/cache/entity/{entityType}/id/{id}
```

## Cache Key Patterns

### User Cache
- `user:id:{id}` - User by ID
- `user:username:{username}` - User by username
- `user:email:{email}` - User by email
- `user:all` - All users
- `user:role:{roleId}` - Users by role

### Category Cache
- `category:id:{id}` - Category by ID
- `category:name:{name}` - Category by name
- `category:all` - All categories
- `category:parent:{parentId}` - Categories by parent

### Item Cache
- `item:id:{id}` - Item by ID
- `item:code:{code}` - Item by code
- `item:name:{name}` - Item by name
- `item:category:{categoryId}` - Items by category
- `item:brand:{brandId}` - Items by brand

## TTL Constants

- `SHORT_TTL` (300s) - 5 minutes
- `MEDIUM_TTL` (1800s) - 30 minutes
- `DEFAULT_TTL` (3600s) - 1 hour
- `LONG_TTL` (7200s) - 2 hours
- `VERY_LONG_TTL` (86400s) - 24 hours

## Best Practices

### 1. Cache Key Design
- Use consistent naming conventions
- Include entity type and identifier
- Consider search parameters for filtered results

### 2. TTL Selection
- Use shorter TTL for frequently changing data
- Use longer TTL for static reference data
- Consider business requirements for data freshness

### 3. Cache Invalidation
- Invalidate specific keys when possible
- Use patterns for bulk invalidation
- Invalidate related caches when updating entities

### 4. Error Handling
- Always handle cache failures gracefully
- Log cache operations for debugging
- Fall back to database when cache is unavailable

### 5. Performance Considerations
- Cache expensive database queries
- Avoid caching large datasets
- Monitor cache hit rates

## Monitoring and Debugging

### Enable Debug Logging
Add to `application.properties`:
```properties
logging.level.com.dtech.admin.util.CacheUtils=DEBUG
logging.level.com.dtech.admin.service.impl.CacheServiceImpl=DEBUG
```

### Cache Statistics
Use the cache management APIs to monitor:
- Cache hit/miss rates
- Memory usage
- Key count

### Redis CLI Commands
```bash
# Connect to Redis
redis-cli

# Check memory usage
INFO memory

# List all keys
KEYS *

# Get key TTL
TTL key_name

# Monitor Redis commands
MONITOR
```

## Troubleshooting

### Common Issues

1. **Connection Refused**
   - Ensure Redis server is running
   - Check host and port configuration

2. **Serialization Errors**
   - Ensure entities are serializable
   - Check for circular references

3. **Memory Issues**
   - Monitor Redis memory usage
   - Adjust TTL settings
   - Implement cache eviction policies

4. **Performance Issues**
   - Check cache hit rates
   - Optimize cache key patterns
   - Review TTL settings

## Migration Guide

### From No Caching to Redis Caching

1. **Add Dependencies**
   - Add Redis dependencies to `pom.xml`

2. **Configure Redis**
   - Add Redis configuration to `application.properties`
   - Create `RedisConfig.java`

3. **Update Services**
   - Inject `CacheUtils` into services
   - Wrap database calls with `getOrCompute()`
   - Add cache invalidation on updates

4. **Test Thoroughly**
   - Verify cache hit/miss behavior
   - Test cache invalidation
   - Monitor performance improvements

## Security Considerations

1. **Redis Security**
   - Use authentication if Redis is exposed
   - Configure firewall rules
   - Use SSL/TLS for remote connections

2. **Data Privacy**
   - Avoid caching sensitive data
   - Implement proper TTL for sensitive information
   - Consider encryption for cached data

3. **Access Control**
   - Restrict cache management APIs
   - Implement proper authentication
   - Audit cache operations

## Future Enhancements

1. **Cache Warming**
   - Pre-populate cache on startup
   - Implement background cache refresh

2. **Distributed Caching**
   - Redis cluster configuration
   - Cache replication

3. **Advanced Features**
   - Cache compression
   - Cache statistics dashboard
   - Automatic cache optimization 
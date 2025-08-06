package com.dtech.admin.util;

public final class CacheConstants {
    
    private CacheConstants() {
        // Utility class
    }
    
    // Cache Names
    public static final String USER_CACHE = "user";
    public static final String ROLE_CACHE = "role";
    public static final String PRIVILEGE_CACHE = "privilege";
    public static final String CATEGORY_CACHE = "category";
    public static final String BRAND_CACHE = "brand";
    public static final String ITEM_CACHE = "item";
    public static final String SUPPLIER_CACHE = "supplier";
    public static final String LOCATION_CACHE = "location";
    public static final String SECTION_CACHE = "section";
    public static final String STOCK_CACHE = "stock";
    public static final String GRN_CACHE = "grn";
    public static final String TRANSFER_CACHE = "transfer";
    public static final String TASK_CACHE = "task";
    public static final String AUDIT_LOG_CACHE = "audit_log";
    
    // Cache Key Patterns
    public static final String USER_BY_ID = "user:id:%d";
    public static final String USER_BY_USERNAME = "user:username:%s";
    public static final String USER_BY_EMAIL = "user:email:%s";
    public static final String ALL_USERS = "user:all";
    public static final String USERS_BY_ROLE = "user:role:%d";
    
    public static final String ROLE_BY_ID = "role:id:%d";
    public static final String ROLE_BY_NAME = "role:name:%s";
    public static final String ALL_ROLES = "role:all";
    
    public static final String PRIVILEGE_BY_ID = "privilege:id:%d";
    public static final String PRIVILEGE_BY_NAME = "privilege:name:%s";
    public static final String ALL_PRIVILEGES = "privilege:all";
    public static final String PRIVILEGES_BY_ROLE = "privilege:role:%d";
    
    public static final String CATEGORY_BY_ID = "category:id:%d";
    public static final String CATEGORY_BY_NAME = "category:name:%s";
    public static final String ALL_CATEGORIES = "category:all";
    public static final String CATEGORIES_BY_PARENT = "category:parent:%d";
    
    public static final String BRAND_BY_ID = "brand:id:%d";
    public static final String BRAND_BY_NAME = "brand:name:%s";
    public static final String ALL_BRANDS = "brand:all";
    
    public static final String ITEM_BY_ID = "item:id:%d";
    public static final String ITEM_BY_CODE = "item:code:%s";
    public static final String ITEM_BY_NAME = "item:name:%s";
    public static final String ALL_ITEMS = "item:all";
    public static final String ITEMS_BY_CATEGORY = "item:category:%d";
    public static final String ITEMS_BY_BRAND = "item:brand:%d";
    public static final String ITEMS_BY_SUPPLIER = "item:supplier:%d";
    
    public static final String SUPPLIER_BY_ID = "supplier:id:%d";
    public static final String SUPPLIER_BY_NAME = "supplier:name:%s";
    public static final String ALL_SUPPLIERS = "supplier:all";
    
    public static final String LOCATION_BY_ID = "location:id:%d";
    public static final String LOCATION_BY_NAME = "location:name:%s";
    public static final String ALL_LOCATIONS = "location:all";
    
    public static final String SECTION_BY_ID = "section:id:%d";
    public static final String SECTION_BY_NAME = "section:name:%s";
    public static final String ALL_SECTIONS = "section:all";
    public static final String SECTIONS_BY_LOCATION = "section:location:%d";
    
    public static final String STOCK_BY_ID = "stock:id:%d";
    public static final String STOCK_BY_ITEM = "stock:item:%d";
    public static final String STOCK_BY_LOCATION = "stock:location:%d";
    public static final String STOCK_BY_ITEM_AND_LOCATION = "stock:item:%d:location:%d";
    public static final String ALL_STOCKS = "stock:all";
    
    public static final String GRN_BY_ID = "grn:id:%d";
    public static final String GRN_BY_SUPPLIER = "grn:supplier:%d";
    public static final String GRN_BY_DATE_RANGE = "grn:date:%s:%s";
    public static final String ALL_GRNS = "grn:all";
    
    public static final String TRANSFER_BY_ID = "transfer:id:%d";
    public static final String TRANSFER_BY_FROM_LOCATION = "transfer:from:%d";
    public static final String TRANSFER_BY_TO_LOCATION = "transfer:to:%d";
    public static final String TRANSFER_BY_DATE_RANGE = "transfer:date:%s:%s";
    public static final String ALL_TRANSFERS = "transfer:all";
    
    public static final String TASK_BY_ID = "task:id:%d";
    public static final String TASK_BY_USER = "task:user:%d";
    public static final String TASK_BY_STATUS = "task:status:%s";
    public static final String ALL_TASKS = "task:all";
    
    public static final String AUDIT_LOG_BY_ID = "audit_log:id:%d";
    public static final String AUDIT_LOG_BY_USER = "audit_log:user:%d";
    public static final String AUDIT_LOG_BY_ACTION = "audit_log:action:%s";
    public static final String AUDIT_LOG_BY_DATE_RANGE = "audit_log:date:%s:%s";
    public static final String ALL_AUDIT_LOGS = "audit_log:all";
    
    // Cache TTL (in seconds)
    public static final long DEFAULT_TTL = 3600; // 1 hour
    public static final long SHORT_TTL = 300; // 5 minutes
    public static final long MEDIUM_TTL = 1800; // 30 minutes
    public static final long LONG_TTL = 7200; // 2 hours
    public static final long VERY_LONG_TTL = 86400; // 24 hours
    
    // Cache Key Separators
    public static final String KEY_SEPARATOR = ":";
    public static final String PATTERN_WILDCARD = "*";
    
    // Cache Key Builders
    public static String buildKey(String pattern, Object... args) {
        return String.format(pattern, args);
    }
    
    public static String buildPattern(String pattern) {
        return pattern + PATTERN_WILDCARD;
    }
} 
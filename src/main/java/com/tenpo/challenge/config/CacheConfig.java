package com.tenpo.challenge.config;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    /** Cache expiration time in minutes. */
    private static final int CACHE_EXPIRATION_MINUTES = 30;
    /** Maximum size of the cache. */
    private static final int CACHE_MAX_SIZE = 1000;

    /**
     * Configures and returns a CacheManager bean
     * that uses Caffeine as the caching provider.
     *
     * @return a CacheManager bean configured with Caffeine caching provider
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager =
            new CaffeineCacheManager("percentage");
        Caffeine caffeine = Caffeine.newBuilder()
                .expireAfterWrite(CACHE_EXPIRATION_MINUTES, TimeUnit.MINUTES)
                .maximumSize(CACHE_MAX_SIZE);
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}

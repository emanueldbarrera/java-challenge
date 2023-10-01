package jp.co.axa.apidemo.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Duration;

@EnableCaching
@Configuration
public class EhCacheConfig {

    public static final String CACHE_GET_EMPLOYEES = "cache_get_employees";
    private static final int DEFAULT_CACHE_DURATION_MINUTES = 1;
    private static final int CACHE_NUMBER_OF_RECORDS = 1000;

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(CACHE_GET_EMPLOYEES,
                    Eh107Configuration.fromEhcacheCacheConfiguration(
                            CacheConfigurationBuilder
                                    .newCacheConfigurationBuilder(PageRequest.class, PageImpl.class, ResourcePoolsBuilder.heap(CACHE_NUMBER_OF_RECORDS))
                                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(DEFAULT_CACHE_DURATION_MINUTES)))
                                    .build()
                    ));
        };
    }
}
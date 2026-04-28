package com.tenpo.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration class for setting up asynchronous processing.
 */
@Configuration
@EnableAsync
public final class AsyncConfig {

    /**
     * Configures a ThreadPoolTaskExecutor for handling asynchronous tasks.
     *
     * @return the configured ThreadPoolTaskExecutor
     */
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        final int corePoolSize = 5;
        final int maxPoolSize = 10;
        final int keepAliveTime = 60;
        final int queueCapacity = 25;
        final String threadNamePrefix = "AsyncExecutor-";
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.initialize();
        return executor;
    }
}

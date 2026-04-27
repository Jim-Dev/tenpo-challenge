package com.tenpo.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Main application class.
 */
@SpringBootApplication
@EnableCaching
@EnableRetry
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class ChallengeApplication {

    /**
     * Main method.
     *
     * @param args arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(ChallengeApplication.class, args);
    }

}

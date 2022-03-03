package com.krzysiekz.loans;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@TestConfiguration
public class IntegrationTestsConfiguration {

    //always return the same date in integration tests
    @Bean
    @Primary
    public Clock clock() {
        return Clock.fixed(OffsetDateTime.of(2020, 1, 1, 12, 1, 1, 1, ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
    }
}

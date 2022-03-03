package com.krzysiekz.loans.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@ComponentScan("com.krzysiekz.loans")
public class LoansApplicationConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}

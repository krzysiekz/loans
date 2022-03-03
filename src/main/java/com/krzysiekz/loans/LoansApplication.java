package com.krzysiekz.loans;

import com.krzysiekz.loans.config.LoansApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoansApplication {

    public static void main(String[] args) {
        SpringApplication.run(new Class[]{LoansApplication.class, LoansApplicationConfiguration.class}, args);
    }

}

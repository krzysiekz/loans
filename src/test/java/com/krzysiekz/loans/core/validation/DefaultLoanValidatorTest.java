package com.krzysiekz.loans.core.validation;

import com.krzysiekz.loans.config.LoanParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultLoanValidatorTest {

    private LoansValidator validator;

    @BeforeEach
    void setUp() {
        final LoanParameters loanParameters = new LoanParameters();
        loanParameters.setMinTerm(5);
        loanParameters.setMaxTerm(10);
        loanParameters.setMinAmount(BigDecimal.ONE);
        loanParameters.setMaxAmount(BigDecimal.TEN);
        loanParameters.setStartRestrictionHour(0);
        loanParameters.setEndRestrictionHour(6);

        validator = new DefaultLoanValidator(loanParameters);
    }

    @Test
    void shouldReturnProperMessageWhenTermIsToLow() {
        //given
        final OffsetDateTime applicationTime = OffsetDateTime.of(2020, 1, 1, 12, 1, 1, 1, ZoneOffset.UTC);
        final BigDecimal amount = new BigDecimal("100");
        final int term = 1;

        //when
        final Optional<String> valid = validator.isValid(term, amount, applicationTime);

        //then
        assertTrue(valid.isPresent());
        assertEquals("Term must be between 5 and 10", valid.get());
    }

    @Test
    void shouldReturnProperMessageWhenTermIsToHigh() {
        //given
        final OffsetDateTime applicationTime = OffsetDateTime.of(2020, 1, 1, 12, 1, 1, 1, ZoneOffset.UTC);
        final BigDecimal amount = new BigDecimal("100");
        final int term = 20;

        //when
        final Optional<String> valid = validator.isValid(term, amount, applicationTime);

        //then
        assertTrue(valid.isPresent());
        assertEquals("Term must be between 5 and 10", valid.get());
    }

    @Test
    void shouldReturnProperMessageWhenAmountIsToLow() {
        //given
        final OffsetDateTime applicationTime = OffsetDateTime.of(2020, 1, 1, 12, 1, 1, 1, ZoneOffset.UTC);
        final BigDecimal amount = BigDecimal.ZERO;
        final int term = 6;

        //when
        final Optional<String> valid = validator.isValid(term, amount, applicationTime);

        //then
        assertTrue(valid.isPresent());
        assertEquals("Amount must be between 1 and 10", valid.get());
    }

    @Test
    void shouldReturnProperMessageWhenAmountIsToHigh() {
        //given
        final OffsetDateTime applicationTime = OffsetDateTime.of(2020, 1, 1, 12, 1, 1, 1, ZoneOffset.UTC);
        final BigDecimal amount = new BigDecimal("100");
        final int term = 6;

        //when
        final Optional<String> valid = validator.isValid(term, amount, applicationTime);

        //then
        assertTrue(valid.isPresent());
        assertEquals("Amount must be between 1 and 10", valid.get());
    }

    @Test
    void shouldReturnProperMessageWhenApplicationTimeIsInvalid() {
        //given
        final OffsetDateTime applicationTime = OffsetDateTime.of(2020, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
        final BigDecimal amount = BigDecimal.TEN;
        final int term = 6;

        //when
        final Optional<String> valid = validator.isValid(term, amount, applicationTime);

        //then
        assertTrue(valid.isPresent());
        assertEquals("Cannot apply for max amount from hour 0 to 6", valid.get());
    }

    @Test
    void shouldReturnEmptyOptionalWhenLoanIsValid() {
        //given
        final OffsetDateTime applicationTime = OffsetDateTime.of(2020, 1, 1, 12, 1, 1, 1, ZoneOffset.UTC);
        final BigDecimal amount = new BigDecimal("2");
        final int term = 5;

        //when
        final Optional<String> valid = validator.isValid(term, amount, applicationTime);

        //then
        assertTrue(valid.isEmpty());
    }
}
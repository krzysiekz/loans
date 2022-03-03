package com.krzysiekz.loans.core.factory;

import com.krzysiekz.loans.config.LoanParameters;
import com.krzysiekz.loans.core.model.Loan;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultLoansFactoryTest {

    @Test
    void shouldCreateProperLoan() {
        //given
        final int term = 10;
        final BigDecimal amount = BigDecimal.TEN;
        final OffsetDateTime applicationTime = OffsetDateTime.of(2020, 1, 1, 12, 1, 1, 1, ZoneOffset.UTC);

        final LoanParameters loanParameters = new LoanParameters();
        loanParameters.setInitialPrincipalPercentage(BigDecimal.TEN);

        final LoansFactory factory = new DefaultLoansFactory(loanParameters);

        //when
        final Loan loan = factory.createLoan(term, amount, applicationTime);

        //then
        assertNotNull(loan);
        assertEquals(applicationTime, loan.getApplicationDateTime());
        assertEquals(0, BigDecimal.ONE.compareTo(loan.getPrincipal()));
        assertEquals(term, loan.getTerm());
        assertEquals(applicationTime.plusDays(term), loan.getDueDate());
        assertEquals(amount, loan.getAmount());
    }
}
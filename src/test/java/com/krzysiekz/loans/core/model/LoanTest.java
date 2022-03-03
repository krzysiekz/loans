package com.krzysiekz.loans.core.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanTest {

    @Test
    void shouldExtendLoanProperly() {
        //given
        final OffsetDateTime applicationTime = OffsetDateTime.of(2020, 1, 1, 12, 1, 1, 1, ZoneOffset.UTC);
        final int term = 10;
        final int extensionTerm = 5;

        final Loan loan = Loan.builder()
                .term(term)
                .principal(BigDecimal.TEN)
                .applicationDateTime(applicationTime)
                .id(1L)
                .dueDate(applicationTime.plusDays(term))
                .build();
        //when
        final Loan extended = loan.extend(extensionTerm);

        //then
        assertEquals(loan.getDueDate().plusDays(extensionTerm), extended.getDueDate());
        assertEquals(term + extensionTerm, extended.getTerm());
    }
}
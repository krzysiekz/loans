package com.krzysiekz.loans.core.service;

import com.krzysiekz.loans.core.factory.LoansFactory;
import com.krzysiekz.loans.core.model.Decision;
import com.krzysiekz.loans.core.model.Loan;
import com.krzysiekz.loans.core.model.LoanDecision;
import com.krzysiekz.loans.core.repository.LoansRepository;
import com.krzysiekz.loans.core.validation.LoansValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultLoansServiceTest {

    LoansValidator loansValidator;
    LoansFactory loansFactory;
    LoansRepository loansRepository;

    LoansService service;

    @BeforeEach
    void setUp() {
        loansValidator = mock(LoansValidator.class);
        loansFactory = mock(LoansFactory.class);
        loansRepository = mock(LoansRepository.class);

        service = new DefaultLoansService(loansValidator, loansFactory, loansRepository, Clock.systemUTC());
    }

    @Test
    void shouldRejectApplicationWhenThereIsValidationProblem() {
        //given
        String errorMessage = "Some error message";

        //when
        when(loansValidator.isValid(any(), any(), any())).thenReturn(Optional.of(errorMessage));
        final LoanDecision decision = service.apply(10, new BigDecimal("20"));

        //then
        assertEquals(Decision.REJECTED, decision.decision());
        assertEquals(errorMessage, decision.errorMessage());
    }

    @Test
    void shouldReturnIdIfLoanWasPersisted() {
        //given
        final Loan loan = Loan.builder().build();
        final long loanId = 1L;

        //when
        when(loansValidator.isValid(any(), any(), any())).thenReturn(Optional.empty());
        when(loansFactory.createLoan(any(), any(), any())).thenReturn(loan);
        when(loansRepository.save(loan)).thenReturn(loanId);

        final LoanDecision decision = service.apply(10, new BigDecimal("20"));

        //then
        assertEquals(Decision.ACCEPTED, decision.decision());
        assertEquals(loanId, decision.loanId());
    }
}
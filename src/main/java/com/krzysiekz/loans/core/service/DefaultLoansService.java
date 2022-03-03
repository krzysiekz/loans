package com.krzysiekz.loans.core.service;

import com.krzysiekz.loans.core.factory.LoansFactory;
import com.krzysiekz.loans.core.model.Loan;
import com.krzysiekz.loans.core.model.LoanDecision;
import com.krzysiekz.loans.core.repository.LoansRepository;
import com.krzysiekz.loans.core.validation.LoansValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class DefaultLoansService implements LoansService {

    private final LoansValidator loansValidator;
    private final LoansFactory loansFactory;
    private final LoansRepository loansRepository;

    private final Clock clock;

    @Override
    public LoanDecision apply(Integer term, BigDecimal amount) {
        final OffsetDateTime applicationTime = OffsetDateTime.now(clock);
        final Optional<String> errorMessageOptional = loansValidator.isValid(term, amount, applicationTime);
        return errorMessageOptional
                .map(LoanDecision::rejected)
                .orElseGet(() -> {
                    final Loan loan = loansFactory.createLoan(term, amount, applicationTime);
                    final Long id = loansRepository.save(loan);
                    return LoanDecision.accepted(id);
                });
    }

    @Override
    public Collection<Loan> getAllLoans() {
        return loansRepository.getAllLoans();
    }

    @Override
    public Optional<Loan> getLoanById(Long loanId) {
        return loansRepository.getById(loanId);
    }

    @Override
    public Optional<Loan> extendLoan(Long loanId) {
        return loansRepository.extend(loanId);
    }
}

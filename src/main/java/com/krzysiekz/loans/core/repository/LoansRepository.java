package com.krzysiekz.loans.core.repository;

import com.krzysiekz.loans.core.model.Loan;

import java.util.Collection;
import java.util.Optional;

public interface LoansRepository {
    Long save(Loan loan);

    Collection<Loan> getAllLoans();

    Optional<Loan> getById(Long loanId);

    Optional<Loan> extend(Long loanId);

    void delete(Long loanId);
}

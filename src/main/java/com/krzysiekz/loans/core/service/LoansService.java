package com.krzysiekz.loans.core.service;


import com.krzysiekz.loans.core.model.Loan;
import com.krzysiekz.loans.core.model.LoanDecision;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

public interface LoansService {
    LoanDecision apply(Integer term, BigDecimal amount);

    Collection<Loan> getAllLoans();

    Optional<Loan> getLoanById(Long loanId);

    Optional<Loan> extendLoan(Long loanId);
}

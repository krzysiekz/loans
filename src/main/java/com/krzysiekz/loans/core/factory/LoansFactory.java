package com.krzysiekz.loans.core.factory;

import com.krzysiekz.loans.core.model.Loan;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface LoansFactory {
    Loan createLoan(Integer term, BigDecimal amount, OffsetDateTime applicationTime);
}

package com.krzysiekz.loans.core.validation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

public interface LoansValidator {
    Optional<String> isValid(Integer term, BigDecimal amount, OffsetDateTime applicationTime);
}

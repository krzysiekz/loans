package com.krzysiekz.loans.core.factory;

import com.krzysiekz.loans.config.LoanParameters;
import com.krzysiekz.loans.core.model.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
class DefaultLoansFactory implements LoansFactory {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100.00");
    private static final int SCALE = 2;

    private final LoanParameters loanParameters;

    @Override
    public Loan createLoan(Integer term, BigDecimal amount, OffsetDateTime applicationDateTime) {
        final BigDecimal amountScaled = amount.setScale(SCALE, RoundingMode.HALF_UP);
        final BigDecimal initialPrincipalPercentageScaled = loanParameters.getInitialPrincipalPercentage()
                .setScale(SCALE, RoundingMode.HALF_UP);

        // assume that there is only getInitialPrincipalPercentage % left to pay
        final BigDecimal initialPrincipal = amountScaled.multiply(initialPrincipalPercentageScaled
                .divide(ONE_HUNDRED, RoundingMode.HALF_UP));
        return Loan.builder()
                .amount(amount)
                .applicationDateTime(applicationDateTime)
                .dueDate(applicationDateTime.plusDays(term))
                .term(term)
                .principal(initialPrincipal)
                .build();
    }
}

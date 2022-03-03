package com.krzysiekz.loans.core.validation;

import com.krzysiekz.loans.config.LoanParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class DefaultLoanValidator implements LoansValidator {

    private final LoanParameters loanParameters;

    @Override
    public Optional<String> isValid(Integer term, BigDecimal amount, OffsetDateTime applicationTime) {

        if (isTermInvalid(term)) {
            return Optional.of(MessageFormat.format("Term must be between {0} and {1}",
                    loanParameters.getMinTerm(), loanParameters.getMaxTerm()));
        }

        if (isAmountInvalid(amount)) {
            return Optional.of(MessageFormat.format("Amount must be between {0} and {1}",
                    loanParameters.getMinAmount(), loanParameters.getMaxAmount()));
        }

        final int hour = applicationTime.getHour();
        if (isApplicationTimeInvalid(amount, hour)) {
            return Optional.of(MessageFormat.format("Cannot apply for max amount from hour {0} to {1}",
                    loanParameters.getStartRestrictionHour(), loanParameters.getEndRestrictionHour()));
        }

        return Optional.empty();
    }

    private boolean isApplicationTimeInvalid(BigDecimal amount, int hour) {
        return (hour >= loanParameters.getStartRestrictionHour() && hour <= loanParameters.getEndRestrictionHour())
                && amount.compareTo(loanParameters.getMaxAmount()) == 0;
    }

    private boolean isAmountInvalid(BigDecimal amount) {
        return amount.compareTo(loanParameters.getMinAmount()) < 0 || amount.compareTo(loanParameters.getMaxAmount()) > 0;
    }

    private boolean isTermInvalid(Integer term) {
        return term < loanParameters.getMinTerm() || term > loanParameters.getMaxTerm();
    }
}

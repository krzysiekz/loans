package com.krzysiekz.loans.core.model;

public record LoanDecision(Decision decision, Long loanId,
                           String errorMessage) {

    public static LoanDecision accepted(Long loanId) {
        return new LoanDecision(Decision.ACCEPTED, loanId, null);
    }

    public static LoanDecision rejected(String errorMessage) {
        return new LoanDecision(Decision.REJECTED, null, errorMessage);
    }

    public boolean isAccepted() {
        return this.decision == Decision.ACCEPTED;
    }
}


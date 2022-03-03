package com.krzysiekz.loans.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Builder
@ToString
public class Loan {
    private final Long id;
    private final OffsetDateTime applicationDateTime;
    private final BigDecimal principal;
    private final Integer term;
    private final OffsetDateTime dueDate;
    private final BigDecimal amount;

    public Loan withId(Long id) {
        return Loan.builder()
                .id(id)
                .applicationDateTime(this.applicationDateTime)
                .principal(this.principal)
                .term(this.getTerm())
                .dueDate(this.dueDate)
                .amount(this.amount)
                .build();
    }

    public Loan extend(Integer extensionTerm) {
        return Loan.builder()
                .id(this.id)
                .applicationDateTime(this.applicationDateTime)
                .principal(this.principal)
                .term(this.getTerm() + extensionTerm)
                .dueDate(this.dueDate.plusDays(extensionTerm))
                .amount(this.amount)
                .build();
    }
}

package com.krzysiekz.loans.rest.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class ApplyForLoanRequest {
    @Positive
    @NotNull
    private Integer term;

    @Positive
    @NotNull
    private BigDecimal amount;
}

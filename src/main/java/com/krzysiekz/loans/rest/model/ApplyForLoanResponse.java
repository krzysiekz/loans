package com.krzysiekz.loans.rest.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Builder
@Data
public class ApplyForLoanResponse {
    private Long loanId;

    private String errorMessage;
}

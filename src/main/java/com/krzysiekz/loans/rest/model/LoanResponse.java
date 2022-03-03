package com.krzysiekz.loans.rest.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Builder
public class LoanResponse {
    private Long id;
    private BigDecimal initialAmount;
    private BigDecimal principal;
    private OffsetDateTime dueDate;
}

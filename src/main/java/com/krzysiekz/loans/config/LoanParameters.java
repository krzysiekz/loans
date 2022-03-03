package com.krzysiekz.loans.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Component
@Validated
@ConfigurationProperties(prefix = "loan")
public class LoanParameters {
    @Positive
    private Integer minTerm;
    @Positive
    private Integer maxTerm;
    @Positive
    private BigDecimal minAmount;
    @Positive
    private BigDecimal maxAmount;
    @Min(value = 0)
    @Max(value = 23)
    private Integer startRestrictionHour;
    @Min(value = 0)
    @Max(value = 23)
    private Integer endRestrictionHour;
    @Min(value = 1L)
    @Max(value = 100L)
    private BigDecimal initialPrincipalPercentage;
    @Positive
    private Integer extensionTerm;
}

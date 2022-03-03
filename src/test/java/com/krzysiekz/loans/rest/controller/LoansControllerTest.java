package com.krzysiekz.loans.rest.controller;

import com.krzysiekz.loans.core.model.Loan;
import com.krzysiekz.loans.core.model.LoanDecision;
import com.krzysiekz.loans.core.service.LoansService;
import com.krzysiekz.loans.rest.model.ApplyForLoanRequest;
import com.krzysiekz.loans.rest.model.ApplyForLoanResponse;
import com.krzysiekz.loans.rest.model.LoanResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = LoansController.class)
class LoansControllerTest {

    @MockBean
    private LoansService loansService;

    @Autowired
    private WebTestClient webClient;

    @Test
    void shouldReturnBadRequestIfApplicationRequestIsInvalid() {
        //given
        final ApplyForLoanRequest request = new ApplyForLoanRequest();
        request.setTerm(-10);
        request.setAmount(BigDecimal.TEN);

        //then
        webClient.post()
                .uri("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), ApplyForLoanRequest.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnBadRequestIfApplicationIsRejected() {
        //given
        final ApplyForLoanRequest request = new ApplyForLoanRequest();
        request.setTerm(5);
        request.setAmount(BigDecimal.TEN);

        final LoanDecision loanDecision = LoanDecision.rejected("Some error");

        //when
        when(loansService.apply(request.getTerm(), request.getAmount())).thenReturn(loanDecision);

        //then
        webClient.post()
                .uri("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), ApplyForLoanRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApplyForLoanResponse.class)
                .value(ApplyForLoanResponse::getErrorMessage, equalTo(loanDecision.errorMessage()));
    }

    @Test
    void shouldReturnLoanIdIfApplicationIsAccepted() {
        //given
        final ApplyForLoanRequest request = new ApplyForLoanRequest();
        request.setTerm(5);
        request.setAmount(BigDecimal.TEN);

        final LoanDecision loanDecision = LoanDecision.accepted(1L);

        //when
        when(loansService.apply(request.getTerm(), request.getAmount())).thenReturn(loanDecision);

        //then
        webClient.post()
                .uri("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), ApplyForLoanRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApplyForLoanResponse.class)
                .value(ApplyForLoanResponse::getLoanId, equalTo(loanDecision.loanId()));
    }

    @Test
    void shouldReturn404IfLoanNotFoundWhileGettingLoan() {
        //when
        when(loansService.getLoanById(1L)).thenReturn(Optional.empty());

        //then
        webClient.get()
                .uri("/loans/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldReturn404IfLoanNotFoundWhileExtendingLoan() {
        //when
        when(loansService.extendLoan(1L)).thenReturn(Optional.empty());

        //then
        webClient.post()
                .uri("/loans/1/extend")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldGetLoanById() {
        //given
        final Loan loan = Loan.builder()
                .dueDate(OffsetDateTime.of(2020, 1, 1, 12, 1, 1, 1, ZoneOffset.UTC))
                .principal(BigDecimal.ONE)
                .id(2L)
                .amount(BigDecimal.TEN)
                .build();

        //when
        when(loansService.getLoanById(1L)).thenReturn(Optional.of(loan));

        //then
        webClient.get()
                .uri("/loans/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoanResponse.class)
                .value(LoanResponse::getId, equalTo(loan.getId()))
                .value(LoanResponse::getDueDate, equalTo(loan.getDueDate()))
                .value(LoanResponse::getInitialAmount, equalTo(loan.getAmount()))
                .value(LoanResponse::getPrincipal, equalTo(loan.getPrincipal()));
    }
}
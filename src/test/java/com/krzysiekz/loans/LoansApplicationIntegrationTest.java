package com.krzysiekz.loans;

import com.krzysiekz.loans.core.model.Loan;
import com.krzysiekz.loans.core.repository.LoansRepository;
import com.krzysiekz.loans.rest.model.ApplyForLoanRequest;
import com.krzysiekz.loans.rest.model.ApplyForLoanResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {LoansApplication.class, IntegrationTestsConfiguration.class},
        properties = {
                "spring.main.allow-bean-definition-overriding=true",
                "loan.min-term=10",
                "loan.max-term=30",
                "loan.min-amount=5",
                "loan.max-amount=10",
                "loan.start-restriction-hour=0",
                "loan.end-restriction-hour=6",
                "loan.initial-principal-percentage=10",
                "loan.extension-term=10"
        })
class LoansApplicationIntegrationTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    private Clock clock;

    @Test
    void shouldCreateNewLoan() {
        //given
        final ApplyForLoanRequest request = new ApplyForLoanRequest();
        request.setTerm(15);
        request.setAmount(BigDecimal.TEN);

        final OffsetDateTime applicationTime = OffsetDateTime.now(clock);

        //when
        final EntityExchangeResult<ApplyForLoanResponse> result = webClient.post()
                .uri("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), ApplyForLoanRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ApplyForLoanResponse.class)
                .value(ApplyForLoanResponse::getLoanId, notNullValue())
                .returnResult();

        //then
        final List<Loan> allLoans = new ArrayList<>(loansRepository.getAllLoans());
        final Long loanId = result.getResponseBody().getLoanId();

        assertThat(allLoans).isNotNull();
        assertThat(allLoans).asList().hasSize(1);
        assertThat(allLoans.get(0).getTerm()).isEqualTo(request.getTerm());
        assertThat(allLoans.get(0).getAmount()).isEqualByComparingTo(request.getAmount());
        assertThat(allLoans.get(0).getId()).isEqualTo(loanId);
        assertThat(allLoans.get(0).getApplicationDateTime()).isEqualTo(applicationTime);
        assertThat(allLoans.get(0).getDueDate()).isEqualTo(applicationTime.plusDays(request.getTerm()));
        assertThat(allLoans.get(0).getPrincipal()).isEqualByComparingTo(BigDecimal.ONE);

        //cleanup
        loansRepository.delete(loanId);
    }

}

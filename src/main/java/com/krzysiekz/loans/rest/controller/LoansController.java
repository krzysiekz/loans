package com.krzysiekz.loans.rest.controller;

import com.krzysiekz.loans.core.model.Loan;
import com.krzysiekz.loans.core.model.LoanDecision;
import com.krzysiekz.loans.core.service.LoansService;
import com.krzysiekz.loans.rest.model.ApplyForLoanRequest;
import com.krzysiekz.loans.rest.model.ApplyForLoanResponse;
import com.krzysiekz.loans.rest.model.LoanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/loans")
public class LoansController {

    private final LoansService loansService;

    @PostMapping
    public Mono<ResponseEntity<ApplyForLoanResponse>> applyForLoan(@Valid @RequestBody ApplyForLoanRequest applyForLoanRequest) {
        final LoanDecision decision = loansService
                .apply(applyForLoanRequest.getTerm(), applyForLoanRequest.getAmount());
        if (decision.isAccepted()) {
            final Long loanId = decision.loanId();
            return Mono.just(ResponseEntity.ok(ApplyForLoanResponse.builder().loanId(loanId).build()));
        } else {
            final String errorMessage = decision.errorMessage();
            return Mono.just(ResponseEntity.badRequest().body(ApplyForLoanResponse.builder().errorMessage(errorMessage).build()));
        }
    }

    @PostMapping("/{loanId}/extend")
    public Mono<ResponseEntity<LoanResponse>> extendLoan(@NotNull @PathVariable Long loanId) {
        return Mono.just(ResponseEntity.of(loansService.extendLoan(loanId).map(this::mapToLoanResponse)));
    }

    @GetMapping
    public Flux<LoanResponse> getAllLoans() {
        return Flux.fromIterable(loansService.getAllLoans().stream()
                .map(this::mapToLoanResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{loanId}")
    public Mono<ResponseEntity<LoanResponse>> getLoanDetails(@NotNull @PathVariable Long loanId) {
        return Mono.just(ResponseEntity.of(loansService.getLoanById(loanId).map(this::mapToLoanResponse)));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    private LoanResponse mapToLoanResponse(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .dueDate(loan.getDueDate())
                .initialAmount(loan.getAmount())
                .principal(loan.getPrincipal())
                .build();
    }
}

package com.krzysiekz.loans.core.repository;

import com.krzysiekz.loans.config.LoanParameters;
import com.krzysiekz.loans.core.model.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class InMemoryLoansRepository implements LoansRepository {

    private final LoanParameters loanParameters;

    final AtomicLong atomicLong = new AtomicLong();
    final Map<Long, Loan> store = new ConcurrentHashMap<>();

    @Override
    public Long save(Loan loan) {
        long id = atomicLong.incrementAndGet();
        store.put(id, loan.withId(id));
        return id;
    }

    @Override
    public Collection<Loan> getAllLoans() {
        return store.values();
    }

    @Override
    public Optional<Loan> getById(Long loanId) {
        return Optional.ofNullable(store.get(loanId));
    }

    @Override
    public Optional<Loan> extend(Long loanId) {
        if (!store.containsKey(loanId)) {
            return Optional.empty();
        }
        final Loan currentState = store.computeIfPresent(loanId,
                (id, oldValue) -> oldValue.extend(loanParameters.getExtensionTerm()));
        return Optional.ofNullable(currentState);
    }

    @Override
    public void delete(Long loanId) {
        store.remove(loanId);
    }
}

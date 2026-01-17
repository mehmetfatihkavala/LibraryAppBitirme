package com.kavala.loan_service.application.query.open;

import com.kavala.loan_service.core.cqrs.QueryHandler;
import com.kavala.loan_service.domain.model.Loan;
import com.kavala.loan_service.domain.port.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Handler for ListOpenLoansQuery.
 */
@Service
@Transactional(readOnly = true)
public class ListOpenLoansHandler implements QueryHandler<ListOpenLoansQuery, List<Loan>> {

    private final LoanRepository loanRepository;

    public ListOpenLoansHandler(LoanRepository loanRepository) {
        this.loanRepository = Objects.requireNonNull(loanRepository, "LoanRepository cannot be null");
    }

    @Override
    public List<Loan> handle(ListOpenLoansQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");
        return loanRepository.findAllOpen();
    }
}

package com.kavala.loan_service.application.query.overdue;

import com.kavala.loan_service.core.cqrs.QueryHandler;
import com.kavala.loan_service.domain.model.Loan;
import com.kavala.loan_service.domain.port.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Handler for ListOverdueLoansQuery.
 */
@Service
@Transactional(readOnly = true)
public class ListOverdueLoansHandler implements QueryHandler<ListOverdueLoansQuery, List<Loan>> {

    private final LoanRepository loanRepository;

    public ListOverdueLoansHandler(LoanRepository loanRepository) {
        this.loanRepository = Objects.requireNonNull(loanRepository, "LoanRepository cannot be null");
    }

    @Override
    public List<Loan> handle(ListOverdueLoansQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");
        return loanRepository.findAllOverdue();
    }
}

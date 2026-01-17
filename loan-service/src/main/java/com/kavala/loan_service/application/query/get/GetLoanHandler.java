package com.kavala.loan_service.application.query.get;

import com.kavala.loan_service.core.cqrs.QueryHandler;
import com.kavala.loan_service.domain.model.Loan;
import com.kavala.loan_service.domain.model.LoanId;
import com.kavala.loan_service.domain.port.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * Handler for GetLoanQuery.
 */
@Service
@Transactional(readOnly = true)
public class GetLoanHandler implements QueryHandler<GetLoanQuery, Optional<Loan>> {

    private final LoanRepository loanRepository;

    public GetLoanHandler(LoanRepository loanRepository) {
        this.loanRepository = Objects.requireNonNull(loanRepository, "LoanRepository cannot be null");
    }

    @Override
    public Optional<Loan> handle(GetLoanQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");
        LoanId loanId = LoanId.of(query.getLoanId());
        return loanRepository.findById(loanId);
    }
}

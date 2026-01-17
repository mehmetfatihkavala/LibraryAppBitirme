package com.kavala.loan_service.application.query.list;

import com.kavala.loan_service.core.cqrs.QueryHandler;
import com.kavala.loan_service.domain.model.Loan;
import com.kavala.loan_service.domain.model.MemberId;
import com.kavala.loan_service.domain.port.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Handler for ListLoansByMemberQuery.
 */
@Service
@Transactional(readOnly = true)
public class ListLoansByMemberHandler implements QueryHandler<ListLoansByMemberQuery, List<Loan>> {

    private final LoanRepository loanRepository;

    public ListLoansByMemberHandler(LoanRepository loanRepository) {
        this.loanRepository = Objects.requireNonNull(loanRepository, "LoanRepository cannot be null");
    }

    @Override
    public List<Loan> handle(ListLoansByMemberQuery query) {
        Objects.requireNonNull(query, "Query cannot be null");
        MemberId memberId = MemberId.of(query.getMemberId());
        return loanRepository.findByMemberId(memberId);
    }
}

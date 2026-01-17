package com.kavala.loan_service.api.rest;

import com.kavala.loan_service.api.rest.dto.LoanDetailResponse;
import com.kavala.loan_service.api.rest.dto.LoanResponse;
import com.kavala.loan_service.api.rest.dto.OverdueLoanResponse;
import com.kavala.loan_service.application.query.get.GetLoanHandler;
import com.kavala.loan_service.application.query.get.GetLoanQuery;
import com.kavala.loan_service.application.query.list.ListLoansByMemberHandler;
import com.kavala.loan_service.application.query.list.ListLoansByMemberQuery;
import com.kavala.loan_service.application.query.open.ListOpenLoansHandler;
import com.kavala.loan_service.application.query.open.ListOpenLoansQuery;
import com.kavala.loan_service.application.query.overdue.ListOverdueLoansHandler;
import com.kavala.loan_service.application.query.overdue.ListOverdueLoansQuery;
import com.kavala.loan_service.domain.model.Loan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * REST Controller for loan query operations.
 * Handles read-only operations for loans.
 */
@RestController
@RequestMapping("/api/loans")
public class LoanQueryController {

    private final GetLoanHandler getLoanHandler;
    private final ListLoansByMemberHandler listLoansByMemberHandler;
    private final ListOpenLoansHandler listOpenLoansHandler;
    private final ListOverdueLoansHandler listOverdueLoansHandler;

    public LoanQueryController(
            GetLoanHandler getLoanHandler,
            ListLoansByMemberHandler listLoansByMemberHandler,
            ListOpenLoansHandler listOpenLoansHandler,
            ListOverdueLoansHandler listOverdueLoansHandler) {
        this.getLoanHandler = Objects.requireNonNull(getLoanHandler);
        this.listLoansByMemberHandler = Objects.requireNonNull(listLoansByMemberHandler);
        this.listOpenLoansHandler = Objects.requireNonNull(listOpenLoansHandler);
        this.listOverdueLoansHandler = Objects.requireNonNull(listOverdueLoansHandler);
    }

    /**
     * Get a loan by ID.
     * GET /api/loans/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoanDetailResponse> getLoan(@PathVariable UUID id) {
        return getLoanHandler.handle(GetLoanQuery.of(id))
                .map(loan -> ResponseEntity.ok(LoanDetailResponse.from(loan)))
                .orElseThrow(() -> new LoanNotFoundException(id));
    }

    /**
     * Get all loans for a member.
     * GET /api/loans/member/{memberId}
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<LoanResponse>> getLoansByMember(@PathVariable UUID memberId) {
        List<Loan> loans = listLoansByMemberHandler.handle(ListLoansByMemberQuery.of(memberId));
        List<LoanResponse> response = loans.stream()
                .map(LoanResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Get all open loans.
     * GET /api/loans/open
     */
    @GetMapping("/open")
    public ResponseEntity<List<LoanResponse>> getOpenLoans() {
        List<Loan> loans = listOpenLoansHandler.handle(ListOpenLoansQuery.instance());
        List<LoanResponse> response = loans.stream()
                .map(LoanResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Get all overdue loans.
     * GET /api/loans/overdue
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<OverdueLoanResponse>> getOverdueLoans() {
        List<Loan> loans = listOverdueLoansHandler.handle(ListOverdueLoansQuery.instance());
        List<OverdueLoanResponse> response = loans.stream()
                .map(OverdueLoanResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Exception thrown when loan is not found.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class LoanNotFoundException extends RuntimeException {
        public LoanNotFoundException(UUID id) {
            super("Loan not found: " + id);
        }
    }
}

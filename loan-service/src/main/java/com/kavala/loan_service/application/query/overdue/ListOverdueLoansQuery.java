package com.kavala.loan_service.application.query.overdue;

import com.kavala.loan_service.core.cqrs.Query;
import com.kavala.loan_service.domain.model.Loan;

import java.util.List;

/**
 * Query to list all overdue loans.
 */
public class ListOverdueLoansQuery implements Query<List<Loan>> {

    private static final ListOverdueLoansQuery INSTANCE = new ListOverdueLoansQuery();

    private ListOverdueLoansQuery() {
    }

    public static ListOverdueLoansQuery instance() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "ListOverdueLoansQuery{}";
    }
}

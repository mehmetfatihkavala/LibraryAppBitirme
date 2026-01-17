package com.kavala.loan_service.application.query.open;

import com.kavala.loan_service.core.cqrs.Query;
import com.kavala.loan_service.domain.model.Loan;

import java.util.List;

/**
 * Query to list all open loans.
 */
public class ListOpenLoansQuery implements Query<List<Loan>> {

    private static final ListOpenLoansQuery INSTANCE = new ListOpenLoansQuery();

    private ListOpenLoansQuery() {
    }

    public static ListOpenLoansQuery instance() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "ListOpenLoansQuery{}";
    }
}

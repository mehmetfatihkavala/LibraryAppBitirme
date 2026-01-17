package com.kavala.loan_service.core.cqrs;

/**
 * Handler for queries.
 * Implements the read logic for a specific query type.
 *
 * @param <Q> the query type
 * @param <R> the return type
 */
public interface QueryHandler<Q extends Query<R>, R> {

    /**
     * Handles the given query.
     *
     * @param query the query to handle
     * @return the result of the query execution
     */
    R handle(Q query);
}

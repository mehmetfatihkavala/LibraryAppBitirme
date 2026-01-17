package com.kavala.loan_service.core.cqrs;

/**
 * Handler for commands.
 * Implements the use case logic for a specific command type.
 *
 * @param <C> the command type
 * @param <R> the return type
 */
public interface CommandHandler<C extends Command<R>, R> {

    /**
     * Handles the given command.
     *
     * @param command the command to handle
     * @return the result of the command execution
     */
    R handle(C command);
}

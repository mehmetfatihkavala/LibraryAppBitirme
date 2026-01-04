package com.kavala.member_service.domain.model;

import java.util.Objects;

public record Name(String firstName, String lastName) {
    public Name {
        Objects.requireNonNull(firstName, "firstName cannot be null");
        Objects.requireNonNull(lastName, "lastName cannot be null");

        if (firstName.isEmpty() || lastName.isEmpty()) {
            throw new IllegalArgumentException("firstName and lastName cannot be empty");
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

}

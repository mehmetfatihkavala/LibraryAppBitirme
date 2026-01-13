package com.kavala.catalog_service.domain.author;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class Author {

    private final AuthorId authorId;
    private final String fullName; // ValueObject olabilir.
    private final LocalDate birthDate;
    private final String nationality; // ValueObject olabilir.
    private final Instant createdAt;

    private Author(AuthorId authorId, String fullName, LocalDate birthDate, String nationality, Instant createdAt) {
        this.authorId = authorId;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.createdAt = createdAt;
    }

    public static Author createAuthor(AuthorId id, String fullName, LocalDate birthDate, String nationality) {
        validateName(fullName);
        validateBirthDate(birthDate);
        validateNationality(nationality);
        return new Author(id, fullName, birthDate, nationality, Instant.now());
    }

    public static Author rehydrate(AuthorId authorId, String fullName, LocalDate birthDate, String nationality,
            Instant createdAt) {
        return new Author(authorId, fullName, birthDate, nationality, createdAt);
    }

    public static Author updateAuthor(Author author, String fullName, LocalDate birthDate, String nationality) {
        validateName(fullName);
        validateBirthDate(birthDate);
        validateNationality(nationality);
        return new Author(author.getAuthorId(), fullName, birthDate, nationality, author.getCreatedAt());
    }

    private static void validateName(String fullName) {
        Objects.requireNonNull(fullName, "FullName cannot be null");
        if (fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("FullName cannot be empty");
        }
        if (fullName.length() > 255) {
            throw new IllegalArgumentException("Name cannot be longer than 255 characters");
        }
    }

    private static void validateBirthDate(LocalDate birthDate) {
        Objects.requireNonNull(birthDate, "BirthDate cannot be null");
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("BirthDate cannot be in the future");
        }
    }

    private static void validateNationality(String nationality) {
        Objects.requireNonNull(nationality, "Nationality cannot be null");
        if (nationality.trim().isEmpty()) {
            throw new IllegalArgumentException("Nationality cannot be empty");
        }
    }

    public AuthorId getAuthorId() {
        return authorId;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getNationality() {
        return nationality;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Author other = (Author) obj;
        return Objects.equals(authorId, other.authorId);
    }
}

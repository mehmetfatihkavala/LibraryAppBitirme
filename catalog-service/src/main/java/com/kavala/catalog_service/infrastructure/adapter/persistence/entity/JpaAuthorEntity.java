package com.kavala.catalog_service.infrastructure.adapter.persistence.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Author domain entity'sinin JPA karşılığı.
 * Veritabanı ile domain model arasında köprü görevi görür.
 */
@Entity
@Table(name = "authors")
public class JpaAuthorEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "nationality", nullable = false, length = 255)
    private String nationality;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected JpaAuthorEntity() {
        // JPA için gerekli
    }

    public JpaAuthorEntity(UUID id, String fullName, LocalDate birthDate, String nationality, Instant createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

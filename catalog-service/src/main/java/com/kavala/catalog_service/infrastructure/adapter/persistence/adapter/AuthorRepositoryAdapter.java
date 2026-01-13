package com.kavala.catalog_service.infrastructure.adapter.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.kavala.catalog_service.domain.author.Author;
import com.kavala.catalog_service.domain.author.AuthorId;
import com.kavala.catalog_service.domain.port.AuthorRepository;
import com.kavala.catalog_service.infrastructure.adapter.persistence.entity.JpaAuthorEntity;
import com.kavala.catalog_service.infrastructure.adapter.persistence.mapper.AuthorMapper;
import com.kavala.catalog_service.infrastructure.adapter.persistence.repo.SpringDataAuthorJpaRepository;

/**
 * AuthorRepository port'unun implementasyonu.
 * Domain katmanını JPA/veritabanı detaylarından izole eder.
 * Hexagonal Architecture'da "driven adapter" rolünü üstlenir.
 */
@Repository
public class AuthorRepositoryAdapter implements AuthorRepository {

    private final SpringDataAuthorJpaRepository jpaRepository;
    private final AuthorMapper mapper;

    public AuthorRepositoryAdapter(SpringDataAuthorJpaRepository jpaRepository, AuthorMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Author save(Author author) {
        JpaAuthorEntity entity = mapper.toEntity(author);
        JpaAuthorEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Author update(Author author) {
        JpaAuthorEntity entity = mapper.toEntity(author);
        JpaAuthorEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Author> findById(AuthorId authorId) {
        return jpaRepository.findById(authorId.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<Author> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Author author) {
        jpaRepository.deleteById(author.getAuthorId().value());
    }

    @Override
    public boolean existsById(AuthorId authorId) {
        return jpaRepository.existsById(authorId.value());
    }
}

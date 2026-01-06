package com.kavala.member_service.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, UUID> {
    boolean existsByEmail(String email);

    boolean existsById(UUID id);

    Optional<MemberEntity> findByEmail(String email);
}

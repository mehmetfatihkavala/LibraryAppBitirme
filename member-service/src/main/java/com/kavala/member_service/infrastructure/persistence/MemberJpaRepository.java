package com.kavala.member_service.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kavala.member_service.domain.model.MemberStatus;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, UUID> {
    boolean existsByEmail(String email);

    boolean existsById(UUID id);

    List<MemberEntity> findByStatus(MemberStatus status);

    Optional<MemberEntity> findByEmail(String email);
}

package com.kavala.member_service.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.kavala.member_service.domain.model.MemberId;
import com.kavala.member_service.domain.model.MemberStatus;
import com.kavala.member_service.domain.port.MemberRepository;
import com.kavala.member_service.infrastructure.persistence.MemberEntity;
import com.kavala.member_service.domain.model.Member;

@Repository
public class MemberRepositoryAdapter implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberMapper memberMapper;

    public MemberRepositoryAdapter(MemberJpaRepository memberJpaRepository, MemberMapper memberMapper) {
        this.memberJpaRepository = memberJpaRepository;
        this.memberMapper = memberMapper;
    }

    @Override
    public Member save(Member member) {
        MemberEntity entity = memberMapper.toEntity(member);
        entity = memberJpaRepository.save(entity);
        return memberMapper.toDomain(entity);
    }

    @Override
    public Member update(Member member) {

        MemberEntity entity = memberMapper.toEntity(member);
        entity = memberJpaRepository.save(entity);
        return memberMapper.toDomain(entity);
    }

    @Override
    public void delete(MemberId id) {
        memberJpaRepository.deleteById(id.value());
    }

    @Override
    public void block(MemberId id) {
        MemberEntity entity = memberJpaRepository.findById(id.value())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        entity.setStatus(MemberStatus.BLOCKED);
        memberJpaRepository.save(entity);
    }

    @Override
    public void unblock(MemberId id) {
        MemberEntity entity = memberJpaRepository.findById(id.value())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        entity.setStatus(MemberStatus.ACTIVE);
        memberJpaRepository.save(entity);
    }

    @Override
    public Optional<Member> findById(MemberId id) {
        return memberJpaRepository.findById(id.value()).map(memberMapper::toDomain);
    }

    @Override
    public List<Member> findAllBlocked() {
        return memberJpaRepository.findAll().stream().filter(entity -> entity.getStatus() == MemberStatus.BLOCKED)
                .map(memberMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Member> findAllActive() {
        return memberJpaRepository.findAll().stream().filter(entity -> entity.getStatus() == MemberStatus.ACTIVE)
                .map(memberMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Member> findAll() {
        return memberJpaRepository.findAll().stream().map(memberMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

}

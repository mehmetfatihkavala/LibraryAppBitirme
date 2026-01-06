package com.kavala.member_service.domain.model;

import java.time.Instant;
import java.util.Objects;

public class Member {
    private final MemberId id;
    private final Name name;
    private final String email;
    private final String phone;
    private final MemberStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Member(MemberId id, Name name, String email, String phone, MemberStatus status, Instant createdAt,
            Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "MemberId cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.phone = Objects.requireNonNull(phone, "Phone cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "UpdatedAt cannot be null");
    }

    public static Member createNewMember(Name name, String email, String phone) {
        validateName(name);
        validateEmail(email);
        validatePhone(phone);
        return new Member(MemberId.generate(), name, email, phone, MemberStatus.ACTIVE, Instant.now(), Instant.now());
    }

    public static Member updateMember(Member member, Name name, String email, String phone) {
        validateName(name);
        validateEmail(email);
        validatePhone(phone);
        return new Member(member.getId(), name, email, phone, member.getStatus(), member.getCreatedAt(), Instant.now());
    }

    private static void validateName(Name name) {
        Objects.requireNonNull(name, "Name cannot be null");
    }

    private static void validateEmail(String email) {
        Objects.requireNonNull(email, "Email cannot be null");
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email is not valid");
        }
    }

    private static void validatePhone(String phone) {
        Objects.requireNonNull(phone, "Phone cannot be null");
        if (phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be empty");
        }
    }

    public Member blockMember() {
        if (this.status == MemberStatus.BLOCKED) {
            throw new IllegalArgumentException("Member is already blocked");
        }
        if (this.status == MemberStatus.DELETED) {
            throw new IllegalArgumentException("Member is already deleted");
        }
        return new Member(id, name, email, phone, MemberStatus.BLOCKED, createdAt, Instant.now());
    }

    public Member activeMember() {
        if (this.status == MemberStatus.ACTIVE) {
            throw new IllegalArgumentException("Member is already active");
        }
        if (this.status == MemberStatus.DELETED) {
            throw new IllegalArgumentException("Member is already deleted");
        }
        return new Member(id, name, email, phone, MemberStatus.ACTIVE, createdAt, Instant.now());
    }

    public Member deleteMember() {
        if (this.status == MemberStatus.DELETED) {
            throw new IllegalArgumentException("Member is already deleted");
        }
        return new Member(id, name, email, phone, MemberStatus.DELETED, createdAt, Instant.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Member member))
            return false;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public MemberId getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}

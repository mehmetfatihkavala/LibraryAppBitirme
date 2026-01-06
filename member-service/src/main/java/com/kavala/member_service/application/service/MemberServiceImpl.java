package com.kavala.member_service.application.service;

import java.util.List;

import com.kavala.member_service.application.dto.CreateMemberCommand;
import com.kavala.member_service.application.dto.UpdateMemberCommand;
import com.kavala.member_service.domain.event.MemberCreatedEvent;
import com.kavala.member_service.domain.event.MemberDeletedEvent;
import com.kavala.member_service.domain.event.MemberUpdatedEvent;
import com.kavala.member_service.domain.exception.DuplicateEmailException;
import com.kavala.member_service.domain.exception.MemberNotFoundException;
import com.kavala.member_service.domain.model.Member;
import com.kavala.member_service.domain.model.Name;
import com.kavala.member_service.domain.model.MemberId;
import com.kavala.member_service.domain.port.MemberRepository;
import org.springframework.context.ApplicationEventPublisher;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MemberServiceImpl(MemberRepository memberRepository, ApplicationEventPublisher eventPublisher) {
        this.memberRepository = memberRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public MemberId createMember(CreateMemberCommand command) {
        if (memberRepository.existsByEmail(command.email())) {
            throw new DuplicateEmailException(command.email());
        }

        Name name = new Name(command.firstName(), command.lastName());
        Member member = Member.createNewMember(name, command.email(), command.phone());
        memberRepository.save(member);

        eventPublisher.publishEvent(new MemberCreatedEvent(member.getId(), member.getName(), member.getEmail(),
                member.getPhone(), member.getCreatedAt()));

        return member.getId();
    }

    @Override
    public void updateMember(UpdateMemberCommand command) {
        Member existingMember = memberRepository.findById(command.memberId())
                .orElseThrow(() -> new MemberNotFoundException(command.memberId()));

        Name name = new Name(command.firstName(), command.lastName());
        Member updatedMember = Member.updateMember(existingMember, name, command.email(), command.phone());

        memberRepository.save(updatedMember);

        eventPublisher.publishEvent(new MemberUpdatedEvent(updatedMember.getId(), updatedMember.getName(),
                updatedMember.getEmail(), updatedMember.getPhone(), updatedMember.getUpdatedAt()));
    }

    @Override
    public void deleteMember(MemberId id) {
        memberRepository.delete(id);
        eventPublisher.publishEvent(new MemberDeletedEvent(id));
    }

    @Override
    public void blockMember(MemberId id) {
        Member member = getMemberById(id);
        member.blockMember();
        memberRepository.save(member);
    }

    @Override
    public Member getMemberById(MemberId id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public void unblockMember(MemberId id) {
        Member member = getMemberById(id);
        member.unblockMember();
        memberRepository.save(member);
    }

}

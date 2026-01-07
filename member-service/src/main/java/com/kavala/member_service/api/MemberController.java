package com.kavala.member_service.api;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kavala.member_service.application.dto.CreateMemberCommand;
import com.kavala.member_service.application.dto.MemberResponse;
import com.kavala.member_service.application.dto.UpdateMemberCommand;
import com.kavala.member_service.application.service.MemberService;
import com.kavala.member_service.domain.model.Member;
import com.kavala.member_service.domain.model.MemberId;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberId> createMember(@RequestBody CreateMemberCommand command) {
        MemberId memberId = memberService.createMember(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMember(@PathVariable UUID id, @RequestBody UpdateMemberCommand command) {
        UpdateMemberCommand commandWithId = new UpdateMemberCommand(
                new MemberId(id),
                command.firstName(),
                command.lastName(),
                command.email(),
                command.phone());
        memberService.updateMember(commandWithId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable UUID id) {
        memberService.deleteMember(new MemberId(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable UUID id) {
        Member member = memberService.getMemberById(new MemberId(id));
        return ResponseEntity.ok(MemberResponse.fromMember(member));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members.stream().map(MemberResponse::fromMember).collect(Collectors.toList()));
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<Void> blockMember(@PathVariable UUID id) {
        memberService.blockMember(new MemberId(id));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/unblock")
    public ResponseEntity<Void> unblockMember(@PathVariable UUID id) {
        memberService.unblockMember(new MemberId(id));
        return ResponseEntity.ok().build();
    }
}

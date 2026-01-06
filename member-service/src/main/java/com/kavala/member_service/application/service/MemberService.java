package com.kavala.member_service.application.service;

import java.util.List;

import com.kavala.member_service.application.dto.CreateMemberCommand;
import com.kavala.member_service.application.dto.UpdateMemberCommand;
import com.kavala.member_service.domain.model.Member;
import com.kavala.member_service.domain.model.MemberId;

public interface MemberService {

    MemberId createMember(CreateMemberCommand command);

    void updateMember(UpdateMemberCommand command);

    void deleteMember(MemberId id);

    void blockMember(MemberId id);

    void unblockMember(MemberId id);

    Member getMemberById(MemberId id);

    List<Member> getAllMembers();
}

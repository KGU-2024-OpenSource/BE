package com.be_provocation.domain.member.service;

import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.domain.member.dto.response.MemberResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {

    public MemberResponse getMyProfile(Member member) {
        return MemberResponse.toDto(member);
    }

}

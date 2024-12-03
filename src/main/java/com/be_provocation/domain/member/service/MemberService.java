package com.be_provocation.domain.member.service;

import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.domain.member.dto.response.MemberResponse;
import com.be_provocation.domain.member.dto.response.NicknameResponse;
import com.be_provocation.domain.member.repository.MemberRepository;
import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse getMyProfile(Member member) {
        return MemberResponse.toDto(member);
    }

    public NicknameResponse getNickname(Long id) {
        Member member  = memberRepository.findById(id)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.MEMBER_NOT_FOUND));
        return new NicknameResponse(member.getNickname());
    }

}

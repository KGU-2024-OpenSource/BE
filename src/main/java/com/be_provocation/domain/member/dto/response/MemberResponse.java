package com.be_provocation.domain.member.dto.response;

import com.be_provocation.domain.member.domain.Member;
import lombok.Builder;

@Builder
public record MemberResponse(
        Long memberId,
        String nickname,
        String profileImageUrl,
        String gender
) {
    public static MemberResponse toDto(Member member) {
        return MemberResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .gender(member.getGender().toString())
                .build();
    }
}

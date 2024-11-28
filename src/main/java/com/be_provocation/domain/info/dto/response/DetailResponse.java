package com.be_provocation.domain.info.dto.response;

import com.be_provocation.domain.info.domain.*;
import com.be_provocation.domain.member.domain.Gender;
import com.be_provocation.domain.member.domain.Member;
import lombok.Builder;

@Builder
public record DetailResponse(
              Long memberId,
              String nickname,
              int birthYear,
              int studentId,
              Gender gender,
              String smokingStatus,
              String snoringStatus,
              String sleepSensitivity,
              MBTI mbti,
              String desiredCloseness,
              String department

        ) {
    public static DetailResponse of (Member member, MyInfo myInfo) {
        return DetailResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .birthYear(myInfo.getBirthYear())
                .studentId(myInfo.getStudentId())
                .gender(member.getGender())
                .smokingStatus(myInfo.getSmokingStatus().getDisplayName())
                .snoringStatus(myInfo.getSnoringStatus().getDisplayName())
                .sleepSensitivity(myInfo.getSleepSensitivity().getDisplayName())
                .mbti(myInfo.getMbti())
                .desiredCloseness(myInfo.getDesiredCloseness().getDisplayName())
                .department(myInfo.getDepartment())
                .build();
    }
}

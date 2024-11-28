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
              SmokingStatus smokingStatus,
              SnoringStatus snoringStatus,
              SleepSensitivity sleepSensitivity,
              MBTI mbti,
              DesiredCloseness desiredCloseness,
              String department

        ) {
    public static DetailResponse of (Member member, MyInfo myInfo) {
        return DetailResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .birthYear(myInfo.getBirthYear())
                .studentId(myInfo.getStudentId())
                .gender(member.getGender())
                .smokingStatus(myInfo.getSmokingStatus())
                .snoringStatus(myInfo.getSnoringStatus())
                .sleepSensitivity(myInfo.getSleepSensitivity())
                .mbti(myInfo.getMbti())
                .desiredCloseness(myInfo.getDesiredCloseness())
                .department(myInfo.getDepartment())
                .build();
    }
}

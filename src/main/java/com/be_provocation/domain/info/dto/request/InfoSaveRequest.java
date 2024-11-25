package com.be_provocation.domain.info.dto.request;

import com.be_provocation.domain.info.domain.DesiredCloseness;
import com.be_provocation.domain.info.domain.MBTI;
import com.be_provocation.domain.info.domain.MyInfo;
import com.be_provocation.domain.info.domain.SleepSensitivity;
import com.be_provocation.domain.info.domain.SmokingStatus;
import com.be_provocation.domain.info.domain.SnoringStatus;
import com.be_provocation.domain.info.domain.YourInfo;
import com.be_provocation.domain.member.domain.Member;

public record InfoSaveRequest(
        MBTI myMBTI,
        Integer myStudentId,
        Integer myBirthYear,
        String mySmokingStatus,
        String mySnoringStatus,
        String mySleepSensitivity,
        String myDepartment,
        String myDesiredCloseness,
        String yourSmokingStatus,
        String yourSnoringStatus,
        String yourSleepSensitivity,
        String yourDepartment
) {
    public MyInfo toMyInfo(Member member) {
        return MyInfo.builder()
                .member(member)
                .studentId(myStudentId)
                .birthYear(myBirthYear)
                .mbti(myMBTI)
                .smokingStatus(SmokingStatus.fromDisplayName(mySmokingStatus))
                .snoringStatus(SnoringStatus.fromDisplayName(mySnoringStatus))
                .sleepSensitivity(SleepSensitivity.fromDisplayName(mySleepSensitivity))
                .department(myDepartment)
                .desiredCloseness(DesiredCloseness.fromDisplayName(myDesiredCloseness))
                .build();
    }

    public YourInfo toYourInfo(Member member) {
        return YourInfo.builder()
                .member(member)
                .smokingStatus(SmokingStatus.fromDisplayName(yourSmokingStatus))
                .snoringStatus(SnoringStatus.fromDisplayName(yourSnoringStatus))
                .sleepSensitivity(SleepSensitivity.fromDisplayName(yourSleepSensitivity))
                .department(yourDepartment)
                .build();
    }
}

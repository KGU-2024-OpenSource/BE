package com.be_provocation.domain.info.dto.response;

import com.be_provocation.domain.info.domain.MBTI;
import com.be_provocation.domain.info.domain.MyInfo;
import com.be_provocation.domain.info.domain.YourInfo;
import lombok.Builder;

@Builder
public record IamYouAreResponse(
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
    public static IamYouAreResponse toDto(MyInfo myInfo, YourInfo yourInfo) {
        return IamYouAreResponse.builder().
                myMBTI(myInfo.getMbti())
                .myStudentId(myInfo.getStudentId())
                .myBirthYear(myInfo.getBirthYear())
                .mySmokingStatus(myInfo.getSmokingStatus().getDisplayName())
                .mySnoringStatus(myInfo.getSnoringStatus().getDisplayName())
                .mySleepSensitivity(myInfo.getSleepSensitivity().getDisplayName())
                .myDepartment(myInfo.getDepartment())
                .myDesiredCloseness(myInfo.getDesiredCloseness().getDisplayName())
                .yourSmokingStatus(yourInfo.getSmokingStatus().getDisplayName())
                .yourSnoringStatus(yourInfo.getSnoringStatus().getDisplayName())
                .yourSleepSensitivity(yourInfo.getSleepSensitivity().getDisplayName())
                .yourDepartment(yourInfo.getDesiredDepartment().getDisplayName())
                .build();

    }
}

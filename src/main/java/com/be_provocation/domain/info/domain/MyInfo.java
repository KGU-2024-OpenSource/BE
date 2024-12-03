package com.be_provocation.domain.info.domain;

import com.be_provocation.domain.info.dto.request.InfoSaveRequest;
import com.be_provocation.domain.info.dto.response.FilteringResponse;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.global.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private int studentId;

    private int birthYear;

    @Enumerated(EnumType.STRING)
    private MBTI mbti; // MBTI

    @Enumerated(EnumType.STRING)
    private SmokingStatus smokingStatus; // 흡연 여부

    @Enumerated(EnumType.STRING)
    private SnoringStatus snoringStatus; // 코골이 여부

    @Enumerated(EnumType.STRING)
    private SleepSensitivity sleepSensitivity; // 잠귀 정도

    private String department; // 학과

    @Enumerated(EnumType.STRING)
    private DesiredCloseness desiredCloseness; // 희망 친밀도

    // request의 특정 값이 null일 경우 기존 값 유지
    public void update(InfoSaveRequest request) {
        Optional.ofNullable(request.myMBTI())
                .ifPresent(value -> this.mbti = value);
        Optional.ofNullable(request.myStudentId())
                .ifPresent(value -> this.studentId = value);
        Optional.ofNullable(request.myBirthYear())
                .ifPresent(value -> this.birthYear = value);
        Optional.ofNullable(request.mySmokingStatus())
                .ifPresent(value -> this.smokingStatus = SmokingStatus.fromDisplayName(value));
        Optional.ofNullable(request.mySnoringStatus())
                .ifPresent(value -> this.snoringStatus = SnoringStatus.fromDisplayName(value));
        Optional.ofNullable(request.mySleepSensitivity())
                .ifPresent(value -> this.sleepSensitivity = SleepSensitivity.fromDisplayName(value));
        Optional.ofNullable(request.myDesiredCloseness())
                .ifPresent(value -> this.desiredCloseness = DesiredCloseness.fromDisplayName(value));
        Optional.ofNullable(request.myDepartment())
                .ifPresent(value -> this.department = value);
    }

    public FilteringResponse toFilteringResponse() {
        return new FilteringResponse(this.id, member.getId(), member.getNickname(), member.getProfileImageUrl(), this.mbti,
                this.studentId, this.birthYear, this.department, this.desiredCloseness.getDisplayName());
    }

}

package com.be_provocation.domain.info.domain;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
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

}

package com.be_provocation.domain.info.service;

import com.be_provocation.domain.info.domain.MyInfo;
import com.be_provocation.domain.info.domain.SleepSensitivity;
import com.be_provocation.domain.info.domain.SmokingStatus;
import com.be_provocation.domain.info.domain.SnoringStatus;
import com.be_provocation.domain.info.domain.YourInfo;
import com.be_provocation.domain.info.dto.request.InfoSaveRequest;
import com.be_provocation.domain.info.dto.response.FilteringResponse;
import com.be_provocation.domain.info.repository.MyInfoRepository;
import com.be_provocation.domain.info.repository.YourInfoRepository;
import com.be_provocation.domain.member.domain.Gender;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final MyInfoRepository myInfoRepository;
    private final YourInfoRepository yourInfoRepository;


    @Transactional
    public void save(InfoSaveRequest request, Member member) {
        MyInfo myInfo = myInfoRepository.findByMember(member).orElseGet(() -> myInfoRepository.save(request.toMyInfo(member)));
        YourInfo yourInfo = yourInfoRepository.findByMember(member).orElseGet(() -> yourInfoRepository.save(request.toYourInfo(member)));

        myInfo.update(request);
        yourInfo.update(request);
    }

    @Transactional(readOnly = true)
    public List<FilteringResponse> filtering(Member member) {
        YourInfo yourInfo = yourInfoRepository.findByMember(member).orElseThrow(() -> CheckmateException.from(ErrorCode.YOURINFO_NOT_FOUND));

        Gender gender = member.getGender();
        SmokingStatus smokingStatus = yourInfo.getSmokingStatus();
        SnoringStatus snoringStatus = yourInfo.getSnoringStatus();
        SleepSensitivity sleepSensitivity = yourInfo.getSleepSensitivity();

        List<MyInfo> findMyInfos = myInfoRepository.findBySmokingStatusAndSnoringStatusAndSleepSensitivity(
                smokingStatus, snoringStatus, sleepSensitivity);

        return findMyInfos.stream()
                .filter(myInfo -> !myInfo.getMember().getId().equals(member.getId())) // 내 정보는 빼고 필터링
                .filter(myInfo -> myInfo.getMember().getGender().equals(gender)) // 동일 성별만 나오도록 필터링
                .map(MyInfo::toFilteringResponse)
                .collect(Collectors.toList());

    }
}

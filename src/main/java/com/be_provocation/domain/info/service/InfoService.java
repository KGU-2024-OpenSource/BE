package com.be_provocation.domain.info.service;

import com.be_provocation.domain.info.domain.MyInfo;
import com.be_provocation.domain.info.domain.SleepSensitivity;
import com.be_provocation.domain.info.domain.SmokingStatus;
import com.be_provocation.domain.info.domain.SnoringStatus;
import com.be_provocation.domain.info.domain.YourInfo;
import com.be_provocation.domain.info.dto.request.InfoSaveRequest;
import com.be_provocation.domain.info.dto.response.DetailResponse;
import com.be_provocation.domain.info.dto.response.FilteringResponse;
import com.be_provocation.domain.info.dto.response.IamYouAreResponse;
import com.be_provocation.domain.info.repository.MyInfoRepository;
import com.be_provocation.domain.info.repository.YourInfoRepository;
import com.be_provocation.domain.member.domain.Gender;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
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
        YourInfo yourInfo;
        try {
            yourInfo = yourInfoRepository.findByMember(member)
                    .orElseThrow(() -> CheckmateException.from(ErrorCode.YOURINFO_NOT_FOUND));
        } catch (CheckmateException e) {
            log.warn("YourInfo not found for member: {}. Fetching all MyInfo.", member.getId());
            // 등록한 "나는 너는"이 없는 경우는 동일성별의 전체 사용자를 반환
            return filterAndMapMyInfos(
                    myInfoRepository.findAll(),
                    member.getId(),
                    member.getGender()
            );
        }

        // 등록한 "나는 너는"이 있다면 최적의 룸메를 상단에, 나머지 사용자를 하단에 보여주도록 조회
        List<MyInfo> prioritizedInfos = getPrioritizedMyInfos(yourInfo);
        return filterAndMapMyInfos(prioritizedInfos, member.getId(), member.getGender());
    }

    private List<MyInfo> getPrioritizedMyInfos(YourInfo yourInfo) {
        List<MyInfo> matchingInfos = myInfoRepository.findBySmokingStatusAndSnoringStatusAndSleepSensitivity(
                yourInfo.getSmokingStatus(),
                yourInfo.getSnoringStatus(),
                yourInfo.getSleepSensitivity()
        );
        List<MyInfo> allInfos = myInfoRepository.findAll();

        // Set을 사용하여 우선순위를 유지하면서 중복 제거
        Set<MyInfo> prioritizedSet = new LinkedHashSet<>(matchingInfos);
        prioritizedSet.addAll(allInfos);

        return new ArrayList<>(prioritizedSet);
    }

    private List<FilteringResponse> filterAndMapMyInfos(List<MyInfo> myInfos, Long memberId, Gender gender) {
        return myInfos.stream()
                .filter(myInfo -> !myInfo.getMember().getId().equals(memberId)) // 내 정보는 제외
                .filter(myInfo -> myInfo.getMember().getGender().equals(gender)) // 동일 성별만
                .map(MyInfo::toFilteringResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public IamYouAreResponse getMyIamYouAreInfo(Member member) {
        MyInfo myInfo = myInfoRepository.findByMember(member).orElseThrow(() -> CheckmateException.from(ErrorCode.MYINFO_NOT_FOUND));
        YourInfo yourInfo = yourInfoRepository.findByMember(member).orElseThrow(() -> CheckmateException.from(ErrorCode.YOURINFO_NOT_FOUND));

        return IamYouAreResponse.toDto(myInfo, yourInfo);
    }

    @Transactional(readOnly = true)
    public DetailResponse getDetails(Long myInfoId) {
        MyInfo roommateInfo = myInfoRepository.findById(myInfoId)
                .orElseThrow(() -> CheckmateException.from(ErrorCode.INFO_NOT_FOUND));
        Member roommate = roommateInfo.getMember();
        log.info("상세보기");
        return DetailResponse.of(roommate, roommateInfo);
    }
}

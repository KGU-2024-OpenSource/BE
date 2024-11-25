package com.be_provocation.domain.info.service;

import com.be_provocation.domain.info.domain.MyInfo;
import com.be_provocation.domain.info.domain.YourInfo;
import com.be_provocation.domain.info.dto.request.InfoSaveRequest;
import com.be_provocation.domain.info.repository.MyInfoRepository;
import com.be_provocation.domain.info.repository.YourInfoRepository;
import com.be_provocation.domain.member.domain.Member;
import java.util.Optional;
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

}

package com.be_provocation.domain.info.repository;

import com.be_provocation.domain.info.domain.MyInfo;
import com.be_provocation.domain.info.domain.SleepSensitivity;
import com.be_provocation.domain.info.domain.SmokingStatus;
import com.be_provocation.domain.info.domain.SnoringStatus;
import com.be_provocation.domain.member.domain.Gender;
import com.be_provocation.domain.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyInfoRepository extends JpaRepository<MyInfo, Long> {
    Optional<MyInfo> findByMember(Member member);

    List<MyInfo> findBySmokingStatusAndSnoringStatusAndSleepSensitivity(
            SmokingStatus smokingStatus,
            SnoringStatus snoringStatus,
            SleepSensitivity sleepSensitivity);
}

package com.be_provocation.domain.info.repository;

import com.be_provocation.domain.info.domain.YourInfo;
import com.be_provocation.domain.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YourInfoRepository extends JpaRepository<YourInfo, Long> {
    Optional<YourInfo> findByMember(Member member);
}

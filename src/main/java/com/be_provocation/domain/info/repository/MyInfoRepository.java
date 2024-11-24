package com.be_provocation.domain.info.repository;

import com.be_provocation.domain.info.domain.MyInfo;
import com.be_provocation.domain.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyInfoRepository extends JpaRepository<MyInfo, Long> {
    Optional<MyInfo> findByMember(Member member);
}

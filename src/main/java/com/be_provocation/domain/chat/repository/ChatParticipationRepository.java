package com.be_provocation.domain.chat.repository;

import com.be_provocation.domain.chat.entity.ChatParticipation;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatParticipationRepository extends JpaRepository<ChatParticipation, Long> {
    // @Query를 사용한 JPQL 쿼리
    @Query("SELECT cp FROM ChatParticipation cp WHERE cp.member.id = :memberId")
    List<ChatParticipation> findAllByMemberId(Long memberId);

    boolean existsByChatRoomAndMember(ChatRoom chatRoom, Member me);
}

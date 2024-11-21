package com.be_provocation.domain.chat.repository;

import com.be_provocation.domain.chat.dto.ChatRoomDto;
import com.be_provocation.domain.chat.entity.ChatParticipation;
import com.be_provocation.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.id IN :chatParticipationList")
    List<ChatRoom> findAllById(List<ChatParticipation> chatParticipationList);
}

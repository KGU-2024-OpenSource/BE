package com.be_provocation.domain.chat.repository;

import com.be_provocation.domain.chat.dto.ChatRoomDto;
import com.be_provocation.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}

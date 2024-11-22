package com.be_provocation.domain.chat.repository;

import com.be_provocation.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query ("select cm from ChatMessage cm where cm.chatRoom.id = :roomId")
    List<ChatMessage> findAllByChatRoomId(Long roomId);

    @Query ("select cm from ChatMessage cm where cm.chatRoom.id = :roomId order by cm.createdAt desc")
    ChatMessage findTopByChatRoomIdOrderByCreatedAtDesc(Long roomId);
}

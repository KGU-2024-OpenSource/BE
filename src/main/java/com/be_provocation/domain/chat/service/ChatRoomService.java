package com.be_provocation.domain.chat.service;

import com.be_provocation.domain.chat.dto.ChatRoomDto;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.chat.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(name)
                .createdAt(LocalDateTime.now())
                .build();
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoomDto getChatRoom(Long roomId) {
        ChatRoom target =  chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        return ChatRoomDto.builder()
                .name(target.getName())
                .createdAt(target.getCreatedAt())
                .build();
    }



}

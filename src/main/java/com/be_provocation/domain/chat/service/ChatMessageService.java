package com.be_provocation.domain.chat.service;

import com.be_provocation.domain.chat.entity.ChatMessage;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.chat.repository.ChatMessageRepository;
import com.be_provocation.domain.chat.repository.ChatRoomRepository;
import com.be_provocation.domain.member.Member;

public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository,
                              ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatMessage saveMessage(Long roomId, Long senderId, String message) {
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."))
                .senderId(new Member(senderId))
                .message(message)
                .build();
        return chatMessageRepository.save(chatMessage);
    }
}

package com.be_provocation.domain.chat.service;

import com.be_provocation.domain.chat.dto.request.ChatMessageReqDto;
import com.be_provocation.domain.chat.dto.response.ChatMessageResDto;
import com.be_provocation.domain.chat.entity.ChatMessage;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.chat.repository.ChatMessageRepository;
import com.be_provocation.domain.chat.repository.ChatRoomRepository;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository,
                              ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatMessageResDto sendMessage(ChatMessageReqDto chatMessageReqDto, Member sender) {

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoomRepository.findById(chatMessageReqDto.getRoom_id()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")))
                .sender(sender)
                .sender_name(sender.getNickname())
                .message(chatMessageReqDto.getMessage())
                .createdAt(LocalDateTime.now())
                .build();

        chatMessageRepository.save(chatMessage);
        return ChatMessageResDto.builder()
                .room_id(chatMessage.getChatRoom().getId())
                .sender_id(chatMessage.getSender().getId())
                .sender_name(chatMessage.getSender_name())
                .message(chatMessage.getMessage())
                .createdAt(chatMessage.getCreatedAt())
                .build();
//        ChatRoom chatRoom = chatMessage.getChatRoom();
//        List<ChatMessage> chatMessages = chatRoom.getChatMessages();
//        chatMessages.add(chatMessage);
//
//        return ChatMessageReqDto.builder()
//                .room_id(chatMessage.getChatRoom().getId())
//                .sender_id(chatMessage.getSender().getId())
//                .sender_name(chatMessage.getSender_name())
//                .message(chatMessage.getMessage())
//                .createdAt(chatMessage.getCreatedAt())
//                .build();
    }

    public List<ChatMessage> getMessagesByRoom(Long roomId, Member member) {
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomId(roomId);
        for (ChatMessage chatMessage : chatMessages) {
            if (!Objects.equals(chatMessage.getSender(), member)) {
                throw new IllegalArgumentException("해당 채팅방에 참여중이지 않습니다.");
            }
        }

        return chatMessages;
    }
}

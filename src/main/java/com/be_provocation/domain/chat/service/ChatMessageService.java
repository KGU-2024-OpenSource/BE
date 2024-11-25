package com.be_provocation.domain.chat.service;

import com.be_provocation.auth.util.CustomUserDetails;
import com.be_provocation.domain.chat.dto.request.ChatMessageReqDto;
import com.be_provocation.domain.chat.dto.response.ChatMessageResDto;
import com.be_provocation.domain.chat.entity.ChatMessage;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.chat.repository.ChatMessageRepository;
import com.be_provocation.domain.chat.repository.ChatRoomRepository;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessageResDto sendMessage(ChatMessageReqDto chatMessageReqDto, CustomUserDetails userDetails) {

        Member sender = userDetails.getMember();
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageReqDto.getRoomId()).orElseThrow(() -> CheckmateException.from(ErrorCode.CHAT_ROOM_NOT_FOUND));
        ChatMessage chatMessage = chatMessageReqDto.toEntity(chatRoom, sender);
        chatMessageRepository.save(chatMessage);

        chatRoom.addMessage(chatMessage);

        return ChatMessageResDto.fromEntity(chatMessage);
    }

    public List<ChatMessageResDto> getMessagesByRoom(Long roomId, CustomUserDetails userDetails) {

        Member member = userDetails.getMember();
        if (member == null) {
            throw CheckmateException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
        }

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> CheckmateException.from(ErrorCode.CHAT_ROOM_NOT_FOUND));
        List<ChatMessage> chatMessagesRoom = chatRoom.getChatMessages();

        return ChatMessageResDto.fromEntities(chatMessagesRoom);
    }

    public Optional<ChatMessage> getLastMessage(Long roomId) {
        return chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(roomId);
    }
}

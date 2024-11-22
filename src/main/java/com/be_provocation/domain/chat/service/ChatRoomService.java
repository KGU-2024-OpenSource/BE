package com.be_provocation.domain.chat.service;

import com.be_provocation.domain.chat.dto.ChatRoomDto;
import com.be_provocation.domain.chat.dto.response.ChatRoomResDto;
import com.be_provocation.domain.chat.entity.ChatMessage;
import com.be_provocation.domain.chat.entity.ChatParticipation;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.chat.repository.ChatMessageRepository;
import com.be_provocation.domain.chat.repository.ChatParticipationRepository;
import com.be_provocation.domain.chat.repository.ChatRoomRepository;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipationRepository chatParticipationRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository,
                           ChatParticipationRepository chatParticipationRepository,
                           MemberRepository memberRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipationRepository = chatParticipationRepository;
        this.memberRepository = memberRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatRoom createChatRoom(ChatRoomDto chatRoomDto) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomDto.getName())
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

    public List<ChatRoomResDto> getAllRooms() {

        List<ChatRoom> list = chatRoomRepository.findAll();
        List<ChatRoomResDto> res = new ArrayList<>();

        for (ChatRoom chatRoom : list) {
            res.add(ChatRoomResDto.builder()
                    .id(chatRoom.getId())
                    .name(chatRoom.getName())
                    .createdAt(chatRoom.getCreatedAt())
                    .build());
        }

        return res;
    }

    public List<ChatRoomResDto> getAllRoom(Member member) {

        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        List<ChatParticipation> chatParticipationList = chatParticipationRepository.findAllByMemberId(member.getId());
        List<ChatRoomResDto> chatRooms = new ArrayList<>();

        for (ChatParticipation chatParticipation : chatParticipationList) {
            Long roomId = chatParticipation.getChatRoom().getId();
            ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
            ChatMessage chatMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(roomId);

            chatRooms.add(ChatRoomResDto.builder()
                    .id(chatRoom.getId())
                    .name(chatRoom.getName())
                    .createdAt(chatRoom.getCreatedAt())
                    .lastMessage(chatMessage != null ? chatMessage.getMessage() : null)
                    .lastMessageAt(chatMessage != null ? chatMessage.getCreatedAt() : null)
                    .build());
        }
        chatRooms.sort((o1, o2) -> o2.getLastMessageAt().compareTo(o1.getLastMessageAt()));
        return chatRooms;
    }
}

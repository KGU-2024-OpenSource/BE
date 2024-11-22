package com.be_provocation.domain.chat.service;

import com.be_provocation.domain.chat.dto.ChatParticipationDto;
import com.be_provocation.domain.chat.entity.ChatParticipation;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.chat.repository.ChatParticipationRepository;
import com.be_provocation.domain.chat.repository.ChatRoomRepository;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.domain.member.repository.MemberRepository;
import com.be_provocation.global.dto.response.ApiResponse;
import com.be_provocation.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatParticipationService {

    private final ChatParticipationRepository chatParticipationRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public ChatParticipationService(ChatParticipationRepository chatParticipationRepository,
                                    ChatRoomRepository chatRoomRepository,
                                    MemberRepository memberRepository) {
        this.chatParticipationRepository = chatParticipationRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
    }

    public ApiResponse<ChatParticipationDto> joinRoom(Long roomId, Member me, String nickname) {
            Member you = memberRepository.findByNickname(nickname).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
            ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

            if (chatParticipationRepository.existsByChatRoomAndMember(chatRoom, me)) {
                return new ApiResponse<>(ErrorCode.CHAT_PARTICIPATION_DUPLICATED);
            }

            chatParticipationRepository.save(ChatParticipation.builder()
                    .chatRoom(chatRoom)
                    .member(me)
                    .build());
            chatParticipationRepository.save(ChatParticipation.builder()
                    .chatRoom(chatRoom)
                    .member(you)
                    .build());

            ChatParticipationDto res = ChatParticipationDto.builder()
                    .roomId(roomId)
                    .memberId(me.getId())
                    .build();

            return new ApiResponse<>(res);
    }

    public ChatParticipation joinRoomByNickname(Long roomId, String nickname) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        Member member = memberRepository.findByNickname(nickname).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return chatParticipationRepository.save(ChatParticipation.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build());
    }
}

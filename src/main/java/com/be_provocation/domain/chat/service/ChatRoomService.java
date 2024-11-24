package com.be_provocation.domain.chat.service;

import com.be_provocation.auth.service.CustomUserDetailService;
import com.be_provocation.domain.chat.dto.response.ChatRoomResDto;
import com.be_provocation.domain.chat.entity.ChatMessage;
import com.be_provocation.domain.chat.entity.ChatParticipation;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.chat.entity.RoomStatus;
import com.be_provocation.domain.chat.repository.ChatRoomRepository;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.global.dto.response.ApiResponse;
import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipationService chatParticipationService;
    private final CustomUserDetailService customUserDetailService;
    private final ChatMessageService chatMessageService;

    /*
    채팅방 생성 + 참여자 추가
    */
    public ChatRoom createChatRoom(Member me, Long youId) {

        if (me == null) {
            throw CheckmateException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
        }
        Member you = customUserDetailService.loadUserById(youId).getMember();

        ChatRoom chatRoom = ChatRoom.builder()
                .createdAt(LocalDateTime.now())
                .build();

        ChatParticipation chatParticipation1 = ChatParticipation.builder()
                .chatRoom(chatRoom)
                .member(me)
                .build();

        ChatParticipation chatParticipation2 = ChatParticipation.builder()
                .chatRoom(chatRoom)
                .member(you)
                .build();

        chatRoom.getParticipation().add(chatParticipation1);
        chatRoom.getParticipation().add(chatParticipation2);

        chatParticipationService.joinRoom(chatRoom.getId(), me, you);
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoomResDto> getAllRoom(Member member) {

        List<ChatParticipation> chatParticipationList = chatParticipationService.getParticipationByMember(member);
        List<ChatRoomResDto> chatRooms = new ArrayList<>();

        for (ChatParticipation chatParticipation : chatParticipationList) {

            Long roomId = chatParticipation.getChatRoom().getId();
            ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> CheckmateException.from(ErrorCode.CHAT_ROOM_NOT_FOUND));
            if (chatRoom.getStatus() == RoomStatus.DEACTIVATE) {
                continue;
            }

            ChatMessage chatMessage = chatMessageService.getLastMessage(roomId);
            List<ChatParticipation> usersInRoom = chatRoom.getParticipation();

            chatRooms.add(ChatRoomResDto.builder()
                    .id(chatRoom.getId())
                    .receiver_name(Objects.equals(usersInRoom.get(0).getId(), member.getId()) ? usersInRoom.get(1).getMember().getNickname() : usersInRoom.get(0).getMember().getNickname())
                    .createdAt(chatRoom.getCreatedAt())
                    .lastMessage(chatMessage != null ? chatMessage.getMessage() : null)
                    .lastMessageAt(chatMessage != null ? chatMessage.getCreatedAt() : null)
                    .build());
        }
        chatRooms.sort((o1, o2) -> o2.getLastMessageAt().compareTo(o1.getLastMessageAt()));
        return chatRooms;
    }

    // 유저들끼리 대화 중 문제가 생길 경우, 확인해야할 수 있으니 soft delete 만 지행
    public void deleteRoom(Long roomId, Member member) {

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> CheckmateException.from(ErrorCode.CHAT_ROOM_NOT_FOUND));
        boolean flag = false;
        for (ChatParticipation chatParticipation : chatRoom.getParticipation()) {
            if (Objects.equals(chatParticipation.getMember().getId(), member.getId())) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw CheckmateException.from(ErrorCode.CHAT_ROOM_NOT_PARTICIPANT);
        }

        chatRoom.updateStatus(RoomStatus.DEACTIVATE); // `deactivate`로 변경하면서 `soft delete`만 진행
    }
}

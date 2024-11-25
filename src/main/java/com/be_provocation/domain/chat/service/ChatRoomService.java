package com.be_provocation.domain.chat.service;

import com.be_provocation.auth.service.CustomUserDetailService;
import com.be_provocation.domain.chat.dto.response.ChatRoomResDto;
import com.be_provocation.domain.chat.entity.ChatMessage;
import com.be_provocation.domain.chat.entity.ChatParticipation;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.chat.entity.RoomStatus;
import com.be_provocation.domain.chat.repository.ChatRoomRepository;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipationService chatParticipationService;
    private final CustomUserDetailService customUserDetailService;
    private final ChatMessageService chatMessageService;

    /*
    채팅방 생성 + 참여자 추가
    */
    public ChatRoomResDto createChatRoom(Member me, Long youId) {

        if (me == null) {
            throw CheckmateException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
        }
        if (Objects.equals(youId, me.getId())) {
            throw CheckmateException.from(ErrorCode.CHAT_ROOM_SELF);
        }

        Member you = customUserDetailService.loadUserById(youId).getMember();

        ChatRoom chatRoom = ChatRoom.builder()
                .status(RoomStatus.ACTIVATE)
                .createdAt(LocalDateTime.now())
                .build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        chatParticipationService.joinRoom(chatRoom, me, you);
        return ChatRoomResDto.fromEntity(savedChatRoom, you.getNickname());
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

            Optional<ChatMessage> chatMessage = chatMessageService.getLastMessage(roomId);
            List<ChatParticipation> usersInRoom = chatRoom.getParticipation();
            String receiverName = Objects.equals(usersInRoom.get(0).getId(), member.getId()) ? usersInRoom.get(0).getMember().getNickname() : usersInRoom.get(1).getMember().getNickname();

            chatRooms.add(ChatRoomResDto.fromEntity(chatRoom, receiverName, chatMessage.orElse(null)));
        }
        chatRooms.sort((o1, o2) -> {
            // o2의 메시지가 null인지 확인하고, null일 경우 생성시간으로 비교
            LocalDateTime time1 = o1.getLastMessageAt() != null ? o1.getLastMessageAt() : o1.getCreatedAt();
            LocalDateTime time2 = o2.getLastMessageAt() != null ? o2.getLastMessageAt() : o2.getCreatedAt();

            return time2.compareTo(time1); // 최근 시간 순으로 정렬
        });
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

    public ChatRoom getChatRoom(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(() -> CheckmateException.from(ErrorCode.CHAT_ROOM_NOT_FOUND));
    }
}

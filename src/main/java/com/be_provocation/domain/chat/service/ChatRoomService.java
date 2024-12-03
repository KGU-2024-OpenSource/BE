package com.be_provocation.domain.chat.service;

import com.be_provocation.auth.service.CustomUserDetailService;
import com.be_provocation.domain.chat.dto.response.ChatRoomResDto;
import com.be_provocation.domain.chat.entity.ChatMessage;
import com.be_provocation.domain.chat.entity.ChatParticipation;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.chat.entity.RoomStatus;
import com.be_provocation.domain.chat.repository.ChatParticipationRepository;
import com.be_provocation.domain.chat.repository.ChatRoomRepository;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
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
    @Transactional
    public ChatRoomResDto createChatRoom(Member me, Long roommateId) {

        if (me == null) {
            throw CheckmateException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
        }
        if (Objects.equals(roommateId, me.getId())) {
            throw CheckmateException.from(ErrorCode.CHAT_ROOM_SELF);
        }

        Member roommate = customUserDetailService.loadUserById(roommateId).getMember();

        // 이미 해당 룸메이트와 채팅방을 만든 적이 있다면 채팅방 정보를 바로 리턴
        List<ChatParticipation> chatParticipationList = chatParticipationService.getParticipationByMember(me);
        for (ChatParticipation chatParticipation : chatParticipationList) {
            if (Objects.equals(chatParticipation.getChatRoom().getParticipation().get(0).getMember().getId(), roommate.getId()) ||
                    Objects.equals(chatParticipation.getChatRoom().getParticipation().get(1).getMember().getId(), roommate.getId())) {
                ChatRoom existingChatRoom = chatParticipation.getChatRoom();
                log.info("이미 존재함.");
                return ChatRoomResDto.fromEntity(existingChatRoom, roommate, chatMessageService.getLastMessage(existingChatRoom.getId()).orElse(null));
            }

        }
        ChatRoom chatRoom = ChatRoom.builder()
                .status(RoomStatus.ACTIVATE)
                .createdAt(LocalDateTime.now())
                .build();

        // 채팅방 생성 후 저장
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // 생성된 채팅방의 참여자로 나랑 룸메를 저장
        chatParticipationService.joinRoom(chatRoom, me, roommate);

        // 아직 대화를 나누지 않아서 chat 내용은 null 처리
        log.info("새로 만들었음.");
        return ChatRoomResDto.fromEntity(savedChatRoom, roommate, null);
    }

    @Transactional
    public List<ChatRoomResDto> getAllRoom(Member me) {

        List<ChatParticipation> chatParticipationList = chatParticipationService.getParticipationByMember(me);
        List<ChatRoomResDto> chatRooms = new ArrayList<>();

        for (ChatParticipation chatParticipation : chatParticipationList) {

            Long roomId = chatParticipation.getChatRoom().getId();
            ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> CheckmateException.from(ErrorCode.CHAT_ROOM_NOT_FOUND));
            if (chatRoom.getStatus() == RoomStatus.DEACTIVATE) {
                continue;
            }

            Optional<ChatMessage> chatMessage = chatMessageService.getLastMessage(roomId);
            List<ChatParticipation> usersInRoom = chatRoom.getParticipation();
            // 룸메 객체를 추출
            Member roommate = Objects.equals(usersInRoom.get(0).getMember().getId(), me.getId()) ? usersInRoom.get(1).getMember() : usersInRoom.get(0).getMember();

            chatRooms.add(ChatRoomResDto.fromEntity(chatRoom, roommate, chatMessage.orElse(null)));
        }
        chatRooms.sort((o1, o2) -> {
            // o2의 메시지가 null인지 확인하고, null일 경우 생성시간으로 비교
            LocalDateTime time1 = o1.getLastMessageAt() != null ? o1.getLastMessageAt() : o1.getCreatedAt();
            LocalDateTime time2 = o2.getLastMessageAt() != null ? o2.getLastMessageAt() : o2.getCreatedAt();

            return time2.compareTo(time1); // 최근 시간 순으로 정렬
        });
        return chatRooms;
    }

    @Transactional
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

    @Transactional
    public ChatRoom getChatRoom(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(() -> CheckmateException.from(ErrorCode.CHAT_ROOM_NOT_FOUND));
    }
}

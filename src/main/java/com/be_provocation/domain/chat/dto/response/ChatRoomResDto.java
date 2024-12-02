package com.be_provocation.domain.chat.dto.response;

import com.be_provocation.domain.chat.entity.ChatMessage;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.member.domain.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomResDto {
    private Long roomId;
    private Long receiverId;
    private String receiverName;
    private String receiverProfileImageUrl;
    private LocalDateTime createdAt;
    private String lastMessage;
    private LocalDateTime lastMessageAt;

    public static ChatRoomResDto fromEntity(ChatRoom savedChatRoom, Member roommate, ChatMessage chatMessage) {
        return ChatRoomResDto.builder()
                .roomId(savedChatRoom.getId())
                .receiverId(roommate.getId())
                .receiverName(roommate.getNickname())
                .receiverProfileImageUrl(roommate.getProfileImageUrl())
                .createdAt(savedChatRoom.getCreatedAt())
                .lastMessage(chatMessage != null ? chatMessage.getMessage() : null)
                .lastMessageAt(chatMessage != null ? chatMessage.getCreatedAt() : null)
                .build();
    }

}

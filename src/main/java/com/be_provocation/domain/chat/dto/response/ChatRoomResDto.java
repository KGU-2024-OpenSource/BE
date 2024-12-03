package com.be_provocation.domain.chat.dto.response;

import com.be_provocation.domain.chat.entity.ChatMessage;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String lastMessage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
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

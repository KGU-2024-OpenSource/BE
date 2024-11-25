package com.be_provocation.domain.chat.dto.response;

import com.be_provocation.domain.chat.dto.request.ChatMessageReqDto;
import com.be_provocation.domain.chat.entity.ChatMessage;
import com.be_provocation.domain.chat.entity.ChatRoom;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomResDto {
    private Long id;
    private String receiver_name;
    private LocalDateTime createdAt;
    private String lastMessage;
    private LocalDateTime lastMessageAt;

    public static ChatRoomResDto fromEntity(ChatRoom savedChatRoom, String receiverName, ChatMessage chatMessage) {
        return ChatRoomResDto.builder()
                .id(savedChatRoom.getId())
                .receiver_name(receiverName)
                .createdAt(savedChatRoom.getCreatedAt())
                .lastMessage(chatMessage != null ? chatMessage.getMessage() : null)
                .lastMessageAt(chatMessage != null ? chatMessage.getCreatedAt() : null)
                .build();
    }

    public static ChatRoomResDto fromEntity(ChatRoom savedChatRoom, String receiverName) {
        return ChatRoomResDto.builder()
                .id(savedChatRoom.getId())
                .receiver_name(receiverName)
                .createdAt(savedChatRoom.getCreatedAt())
                .lastMessage(null)
                .lastMessageAt(null)
                .build();
    }
}

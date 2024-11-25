package com.be_provocation.domain.chat.dto.response;

import com.be_provocation.domain.chat.entity.ChatMessage;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResDto {
    @Size(min = 1, max = 200, message = "메시지는 최소 1자, 최대 200자까지 입력 가능합니다.")
    private String message;

    private Long sender_id;

    private Long room_id;

    private String sender_name;

    private LocalDateTime createdAt;

    public static ChatMessageResDto fromEntity(ChatMessage chatMessage) {
        return ChatMessageResDto.builder()
                .room_id(chatMessage.getChatRoom().getId())
                .sender_id(chatMessage.getSender().getId())
                .sender_name(chatMessage.getSender_name())
                .message(chatMessage.getMessage())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }

    public static List<ChatMessageResDto> fromEntities(List<ChatMessage> chatMessages) {
        return chatMessages.stream()
                .map(ChatMessageResDto::fromEntity)
                .toList();
    }
}

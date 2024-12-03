package com.be_provocation.domain.chat.dto.response;

import com.be_provocation.domain.chat.entity.ChatMessage;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResDto {
    @Size(min = 1, max = 200, message = "메시지는 최소 1자, 최대 200자까지 입력 가능합니다.")
    private String message;

    private Long senderId;

    private Long roomId;

    private String senderName;

    private String senderProfileImageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static ChatMessageResDto fromEntity(ChatMessage chatMessage) {
        return ChatMessageResDto.builder()
                .roomId(chatMessage.getChatRoom().getId())
                .senderId(chatMessage.getSender().getId())
                .senderName(chatMessage.getSender_name())
                .senderProfileImageUrl(chatMessage.getSender().getProfileImageUrl())
                .message(chatMessage.getMessage())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }

    public static List<ChatMessageResDto> fromEntitieList(List<ChatMessage> chatMessages) {
        return chatMessages.stream()
                .map(ChatMessageResDto::fromEntity)
                .toList();
    }
}

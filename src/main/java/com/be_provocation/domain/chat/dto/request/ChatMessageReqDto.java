package com.be_provocation.domain.chat.dto.request;

import com.be_provocation.domain.chat.entity.ChatMessage;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageReqDto {
    @NotBlank(message = "메시지는 필수입니다.")
    @Size(min = 1, max = 200, message = "메시지는 최소 1자, 최대 200자까지 입력 가능합니다.")
    private String message;

    private Long roomId;

    public ChatMessage toEntity(ChatRoom chatRoom, Member sender) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .sender_name(sender.getNickname())
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();
    }
}

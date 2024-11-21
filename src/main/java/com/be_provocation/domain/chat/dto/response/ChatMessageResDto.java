package com.be_provocation.domain.chat.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResDto {
    @Size(min = 1, max = 200, message = "메시지는 최소 1자, 최대 200자까지 입력 가능합니다.")
    private String message;

    private Long sender_id;

    @NotBlank(message = "채팅방 번호는 필수입니다.")
    private Long room_id;

    private String sender_name;

    private LocalDateTime createdAt;
}

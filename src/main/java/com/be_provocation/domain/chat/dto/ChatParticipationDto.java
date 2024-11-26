package com.be_provocation.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatParticipationDto {
    @NotBlank(message = "채팅방 ID는 필수입니다.")
    private Long roomId;

    @NotBlank(message = "사용자 ID는 필수입니다.")
    private Long roomMateId;
}

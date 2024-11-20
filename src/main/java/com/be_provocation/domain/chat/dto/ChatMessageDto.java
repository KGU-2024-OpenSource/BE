package com.be_provocation.domain.chat.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    private String content;
    private String sender;
    private String receiver;
    private String time;
}

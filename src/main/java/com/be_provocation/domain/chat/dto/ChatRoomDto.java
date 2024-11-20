package com.be_provocation.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
public class ChatRoomDto {
    private String name;
    private LocalDateTime createdAt;
    public ChatRoomDto(String name, LocalDateTime createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }
}

package com.be_provocation.domain.chat.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDto {

    @Size(min = 1, max = 20)
    private String name;

    private LocalDateTime createdAt;


//    public ChatRoomDto(String name, LocalDateTime createdAt) {
//        this.name = name;
//        this.createdAt = createdAt;
//    }
}

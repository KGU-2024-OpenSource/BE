package com.be_provocation.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}

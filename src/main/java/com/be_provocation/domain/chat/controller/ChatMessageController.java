package com.be_provocation.domain.chat.controller;

import com.be_provocation.auth.util.CustomUserDetails;
import com.be_provocation.domain.chat.dto.request.ChatMessageReqDto;
import com.be_provocation.domain.chat.dto.response.ChatMessageResDto;
import com.be_provocation.domain.chat.entity.ChatMessage;
import com.be_provocation.domain.chat.service.ChatMessageService;
import com.be_provocation.domain.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping("/send")
    @Operation(summary = "메시지 생성 API", description = "메시지를 생성하는 API입니다.")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatMessageResDto sendMessage(@Valid @RequestBody ChatMessageReqDto chatMessageDto,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        Member member = userDetails.getMember();
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        return chatMessageService.sendMessage(chatMessageDto, member);
    }

    @GetMapping("/all/{roomId}")
    @Operation(summary = "채팅방 전체 메시지 조회 API", description = "특정 채팅방의 모든 메시지를 조회하는 API입니다.")
    @ResponseStatus(HttpStatus.OK)
    public List<ChatMessage> getAllMessagesByRoom(@PathVariable Long roomId,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {

        Member member = userDetails.getMember();
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        return chatMessageService.getMessagesByRoom(roomId, member);
    }
}
package com.be_provocation.domain.chat.controller;

import com.be_provocation.auth.util.CustomUserDetails;
import com.be_provocation.domain.chat.dto.ChatParticipationDto;
import com.be_provocation.domain.chat.entity.ChatParticipation;
import com.be_provocation.domain.chat.service.ChatParticipationService;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.global.dto.response.ApiResponse;
import com.be_provocation.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "ChatParticipationController", description = "채팅방 참여 API")
@RequestMapping("/participation")
public class ChatParticipationController {

    private final ChatParticipationService chatParticipationService;

    public ChatParticipationController(ChatParticipationService chatParticipationService) {
        this.chatParticipationService = chatParticipationService;
    }

    @PostMapping("/join/{roomId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "채팅방 참여 API", description = "`대화하러 가기` 눌렀을 때, 두명 모두 채팅방에 참여하는 API입니다." +
            "\n- nickname: 상대방의 닉네임" +
            "\n- roomId: 채팅방의 id")
    public ApiResponse<ChatParticipationDto> joinRoom(@PathVariable Long roomId,
                                            @RequestParam String nickname,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getMember() == null) {
            throw new IllegalArgumentException("로그인 정보가 올바르지 않습니다.");
        }
        Member member = userDetails.getMember();
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        log.info("member_id: {}", member.getId());
        log.info("roomId: {}", roomId);
        log.info("nickname: {}", nickname);
        ApiResponse<ChatParticipationDto> res = chatParticipationService.joinRoom(roomId, member, nickname);
        if (res == null) {
            return new ApiResponse<>(ErrorCode.CHAT_PARTICIPATION_FAILED);
        }
        return res;
    }
}


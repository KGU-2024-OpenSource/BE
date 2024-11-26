package com.be_provocation.domain.chat.controller;

import com.be_provocation.auth.util.CustomUserDetails;
import com.be_provocation.domain.chat.dto.ChatRoomDto;
import com.be_provocation.domain.chat.dto.response.ChatRoomResDto;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.chat.service.ChatRoomService;
import com.be_provocation.global.domain.SuccessCode;
import com.be_provocation.global.dto.response.ApiResponse;
import com.be_provocation.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "채팅방 생성 API", description = "'대화 참여하기' 눌렀을 때, 채팅방을 생성하는 API입니다.")
    public ApiResponse<ChatRoomResDto> crateRoom(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @RequestParam Long youId) {
        return new ApiResponse<>(chatRoomService.createChatRoom(userDetails.getMember(), youId));
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "참여중인 채팅방 조회 API", description = "본인이 참여중인 채팅방을 조회하는 API입니다.")
    public ApiResponse<List<ChatRoomResDto>> getMessagesByRoom(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return new ApiResponse<List<ChatRoomResDto>>(chatRoomService.getAllRoom(userDetails.getMember()));
    }

    @DeleteMapping("/delete/{roomId}")
    @Operation(summary = "채팅방 나가기(삭제) API", description = "채팅방을 나가기(삭제)하는 API입니다.")
    public ApiResponse deleteRoom(@PathVariable Long roomId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        chatRoomService.deleteRoom(roomId, userDetails.getMember());
        return new ApiResponse<>(SuccessCode.REQUEST_OK);
    }
}

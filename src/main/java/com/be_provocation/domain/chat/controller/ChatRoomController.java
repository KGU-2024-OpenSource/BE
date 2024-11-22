package com.be_provocation.domain.chat.controller;

import com.be_provocation.auth.util.CustomUserDetails;
import com.be_provocation.domain.chat.dto.ChatRoomDto;
import com.be_provocation.domain.chat.dto.response.ChatRoomResDto;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.chat.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "채팅방 생성 API", description = "'대화 참여하기' 눌렀을 때, 채팅방을 생성하는 API입니다.")
    public ChatRoom crateRoom(String chatRoomName) {
        return chatRoomService.createChatRoom(ChatRoomDto.builder()
                .name(chatRoomName)
                .build());
    }

    @GetMapping("/get/list")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "참여중인 채팅방 조회 API", description = "본인이 참여중인 채팅방을 조회하는 API입니다.")
    public List<ChatRoomResDto> getMessagesByRoom(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return chatRoomService.getAllRoom(userDetails.getMember());
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "모든 채팅방 조회", description = "데이터베이스의 모든 채팅방을 조회하는 API")
    public List<ChatRoomResDto> getAllRooms() {
        return chatRoomService.getAllRooms();
    }
}

package com.be_provocation.domain.chat.controller;


import com.be_provocation.domain.chat.service.ChatParticipationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "ChatParticipationController", description = "채팅방 참여 API")
@RequestMapping("/participation")
@RequiredArgsConstructor
public class ChatParticipationController {

    private final ChatParticipationService chatParticipationService;
}


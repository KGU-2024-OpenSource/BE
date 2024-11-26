package com.be_provocation.global.handler;

import com.be_provocation.domain.chat.dto.response.ChatMessageResDto;
import com.be_provocation.global.exception.CheckmateException;
import com.be_provocation.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ChatWebSocketChatHandler extends TextWebSocketHandler {

    private final Map<Long, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long roomId = extractRoomId(session);
        roomSessions.computeIfAbsent(roomId, key -> ConcurrentHashMap.newKeySet()).add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        Long roomId = extractRoomId(session);
        String payload = message.getPayload();
        log.info("roomId: {}, payload: {}", roomId, payload);

        ChatMessageResDto messageResDto = mapper.readValue(payload, ChatMessageResDto.class);// JSON 페이로드 파싱
//        messageResDto.setCreatedAt(LocalDateTime.now());
        log.info("Parsed message: senderName={}, message={}, senderId={}, roomId={}",
                messageResDto.getSenderName(), messageResDto.getMessage(), messageResDto.getSenderId(), messageResDto.getRoomId());

        // 메시지 브로드캐스트
        roomSessions.get(roomId).forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(new TextMessage(mapper.writeValueAsString(messageResDto)));
            } catch (IOException e) {
                log.error("Error sending message to session {}: {}", webSocketSession.getId(), e.getMessage());
                throw CheckmateException.from(ErrorCode.CHAT_MESSAGE_SEND_FAILED);
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long roomId = extractRoomId(session);
        roomSessions.get(roomId).remove(session);
    }

    private Long extractRoomId(WebSocketSession session) {
        String uri = session.getUri().toString();
        return Long.valueOf(uri.substring(uri.lastIndexOf("/") + 1));
    }
}

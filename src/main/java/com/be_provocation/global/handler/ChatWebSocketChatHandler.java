package com.be_provocation.global.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ChatWebSocketChatHandler extends TextWebSocketHandler {

    private final Map<Long, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

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

        roomSessions.get(roomId).forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // 메시지 브로드캐스트
    public void broadcastMessage(Long roomId, String message) {
        if (roomSessions.containsKey(roomId)) {
            roomSessions.get(roomId).forEach(session -> {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
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

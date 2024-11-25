package com.be_provocation.domain.chat.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private RoomStatus status; // 채팅방 상태

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatParticipation> participation;// 채팅방의 참여 정보

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ChatMessage> messages;

    public List<ChatMessage> getChatMessages() {
        return messages;
    }

    public void addParticipation(ChatParticipation chatParticipation) {
        if (participation == null) {
            participation = new ArrayList<>();
        }
        participation.add(chatParticipation);
    }

    public void addMessage(ChatMessage chatMessage) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(chatMessage);
    }

    public void updateStatus(RoomStatus roomStatus) {
        this.status = roomStatus;
    }
}

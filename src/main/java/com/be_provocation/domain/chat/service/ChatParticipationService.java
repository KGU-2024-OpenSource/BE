package com.be_provocation.domain.chat.service;

import com.be_provocation.domain.chat.entity.ChatParticipation;
import com.be_provocation.domain.chat.entity.ChatRoom;
import com.be_provocation.domain.chat.repository.ChatParticipationRepository;
import com.be_provocation.domain.chat.repository.ChatRoomRepository;
import com.be_provocation.domain.member.domain.Member;
import com.be_provocation.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatParticipationService {

    private final ChatParticipationRepository chatParticipationRepository;

    public void joinRoom(ChatRoom chatRoom, Member me, Member you) {
        ChatParticipation participation1 = chatParticipationRepository.save(ChatParticipation.builder()
                                            .chatRoom(chatRoom)
                                            .member(me)
                                            .build());
        ChatParticipation participation2 = chatParticipationRepository.save(ChatParticipation.builder()
                                            .chatRoom(chatRoom)
                                            .member(you)
                                            .build());
        chatRoom.addParticipation(participation1);
        chatRoom.addParticipation(participation2);
    }

    public List<ChatParticipation> getParticipationByMember(Member member) {
        return chatParticipationRepository.findAllByMemberId(member.getId());
    }
}

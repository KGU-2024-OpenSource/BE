package com.be_provocation.domain.chat.repository;

import com.be_provocation.domain.chat.entity.ChatParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipationRepository extends JpaRepository<ChatParticipation, Long> {
}

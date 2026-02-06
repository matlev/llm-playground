package com.llmplayground.dm.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySessionIdOrderBySequenceIndexAsc(Long sessionId);
    Optional<Message> findTopBySessionIdOrderBySequenceIndexDesc(Long sessionId);
}

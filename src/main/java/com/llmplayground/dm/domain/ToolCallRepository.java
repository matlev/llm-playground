package com.llmplayground.dm.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolCallRepository extends JpaRepository<ToolCall, Long> {
    List<ToolCall> findBySessionIdOrderBySequenceIndexAsc(Long sessionId);
    Optional<ToolCall> findTopBySessionIdOrderBySequenceIndexDesc(Long sessionId);
}

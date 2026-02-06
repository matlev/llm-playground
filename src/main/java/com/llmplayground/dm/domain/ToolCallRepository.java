package com.llmplayground.dm.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolCallRepository extends JpaRepository<ToolCall, Long> {
    List<ToolCall> findBySessionIdOrderByCreatedAtAsc(Long sessionId);
}

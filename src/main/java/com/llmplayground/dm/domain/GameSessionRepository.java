package com.llmplayground.dm.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    List<GameSession> findByCampaignId(Long campaignId);
}

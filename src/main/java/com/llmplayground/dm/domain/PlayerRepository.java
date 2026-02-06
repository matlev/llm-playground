package com.llmplayground.dm.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByCampaignId(Long campaignId);
}

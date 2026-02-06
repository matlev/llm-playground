package com.llmplayground.dm.application;

import com.llmplayground.dm.domain.Campaign;
import com.llmplayground.dm.domain.CampaignRepository;
import com.llmplayground.dm.domain.Player;
import com.llmplayground.dm.domain.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final CampaignRepository campaignRepository;

    public PlayerService(PlayerRepository playerRepository, CampaignRepository campaignRepository) {
        this.playerRepository = playerRepository;
        this.campaignRepository = campaignRepository;
    }

    @Transactional
    public Player createPlayer(Long campaignId, String name, String characterName) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaign not found"));

        Player player = new Player();
        player.setCampaign(campaign);
        player.setName(name);
        player.setCharacterName(characterName);
        return playerRepository.save(player);
    }
}

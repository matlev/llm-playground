package com.llmplayground.dm.application;

import com.llmplayground.dm.domain.Campaign;
import com.llmplayground.dm.domain.CampaignRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public List<Campaign> listCampaigns() {
        return campaignRepository.findAll();
    }

    @Transactional
    public Campaign createCampaign(String name, String description) {
        Campaign campaign = new Campaign();
        campaign.setName(name);
        campaign.setDescription(description);
        return campaignRepository.save(campaign);
    }
}

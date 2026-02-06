package com.llmplayground.dm.api;

import com.llmplayground.dm.application.CampaignService;
import com.llmplayground.dm.domain.Campaign;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public List<Campaign> listCampaigns() {
        return campaignService.listCampaigns();
    }

    @PostMapping
    public Campaign createCampaign(@Valid @RequestBody CreateCampaignRequest request) {
        return campaignService.createCampaign(request.name(), request.description());
    }

    public record CreateCampaignRequest(@NotBlank String name, String description) {}
}

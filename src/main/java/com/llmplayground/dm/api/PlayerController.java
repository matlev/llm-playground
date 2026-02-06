package com.llmplayground.dm.api;

import com.llmplayground.dm.application.PlayerService;
import com.llmplayground.dm.domain.Player;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public Player createPlayer(@Valid @RequestBody CreatePlayerRequest request) {
        return playerService.createPlayer(request.campaignId(), request.name(), request.characterName());
    }

    public record CreatePlayerRequest(@NotNull Long campaignId, @NotBlank String name, @NotBlank String characterName) {
    }
}

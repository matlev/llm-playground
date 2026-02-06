package com.llmplayground.dm.api;

import com.llmplayground.dm.application.SessionService;
import com.llmplayground.dm.domain.GameSession;
import com.llmplayground.dm.domain.Message;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public GameSession createSession(@Valid @RequestBody CreateSessionRequest request) {
        return sessionService.createSession(request.campaignId(), request.title());
    }

    @PostMapping("/{sessionId}/messages")
    public Message postPlayerMessage(@PathVariable Long sessionId,
                                     @Valid @RequestBody PostMessageRequest request) {
        return sessionService.postPlayerMessage(sessionId, request.playerId(), request.content());
    }

    @GetMapping("/{sessionId}/state")
    public SessionService.SessionStateView getSessionState(@PathVariable Long sessionId) {
        return sessionService.getSessionState(sessionId);
    }

    public record CreateSessionRequest(@NotNull Long campaignId, @NotBlank String title) {}
    public record PostMessageRequest(Long playerId, @NotBlank String content) {}
}

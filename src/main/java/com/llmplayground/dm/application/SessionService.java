package com.llmplayground.dm.application;

import com.llmplayground.dm.domain.Campaign;
import com.llmplayground.dm.domain.CampaignRepository;
import com.llmplayground.dm.domain.GameSession;
import com.llmplayground.dm.domain.GameSessionRepository;
import com.llmplayground.dm.domain.Message;
import com.llmplayground.dm.domain.MessageRepository;
import com.llmplayground.dm.domain.MessageRole;
import com.llmplayground.dm.domain.Player;
import com.llmplayground.dm.domain.PlayerRepository;
import com.llmplayground.dm.infrastructure.llm.LLMClient;
import com.llmplayground.dm.tools.ToolRegistry;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessionService {

    private final GameSessionRepository sessionRepository;
    private final CampaignRepository campaignRepository;
    private final MessageRepository messageRepository;
    private final PlayerRepository playerRepository;
    private final LLMClient llmClient;
    private final ToolRegistry toolRegistry;

    public SessionService(GameSessionRepository sessionRepository,
                          CampaignRepository campaignRepository,
                          MessageRepository messageRepository,
                          PlayerRepository playerRepository,
                          LLMClient llmClient,
                          ToolRegistry toolRegistry) {
        this.sessionRepository = sessionRepository;
        this.campaignRepository = campaignRepository;
        this.messageRepository = messageRepository;
        this.playerRepository = playerRepository;
        this.llmClient = llmClient;
        this.toolRegistry = toolRegistry;
    }

    @Transactional
    public GameSession createSession(Long campaignId, String title) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaign not found"));
        GameSession session = new GameSession();
        session.setCampaign(campaign);
        session.setTitle(title);
        return sessionRepository.save(session);
    }

    @Transactional
    public Message postPlayerMessage(Long sessionId, Long playerId, String content) {
        GameSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
        Player player = playerId == null ? null : playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found"));

        String correlationId = MDC.get("correlationId") == null ? "no-correlation-id" : MDC.get("correlationId");
        long nextSequence = nextSequenceIndex(sessionId);

        Message playerMessage = new Message();
        playerMessage.setSession(session);
        playerMessage.setPlayer(player);
        playerMessage.setRole(MessageRole.PLAYER);
        playerMessage.setContent(content);
        playerMessage.setSequenceIndex(nextSequence);
        playerMessage.setCorrelationId(correlationId);
        Message savedPlayerMessage = messageRepository.save(playerMessage);

        LLMClient.LLMResponse response = llmClient.generate(new LLMClient.LLMRequest(
                "placeholder-model",
                List.of(new LLMClient.PromptMessage("player", content)),
                toolRegistry.listTools().stream()
                        .map(t -> new LLMClient.ToolDefinition(t.name(), t.description(), t.inputSchemaJson()))
                        .toList()));

        Message dmMessage = new Message();
        dmMessage.setSession(session);
        dmMessage.setRole(MessageRole.DM);
        dmMessage.setContent(response.narrativeText());
        dmMessage.setSequenceIndex(nextSequence + 1);
        dmMessage.setCorrelationId(correlationId);
        messageRepository.save(dmMessage);

        return savedPlayerMessage;
    }

    @Transactional(readOnly = true)
    public SessionStateView getSessionState(Long sessionId) {
        GameSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
        List<Message> messages = messageRepository.findBySessionIdOrderBySequenceIndexAsc(sessionId);
        return new SessionStateView(session, messages);
    }

    private long nextSequenceIndex(Long sessionId) {
        return messageRepository.findTopBySessionIdOrderBySequenceIndexDesc(sessionId)
                .map(message -> message.getSequenceIndex() + 1)
                .orElse(1L);
    }

    public record SessionStateView(GameSession session, List<Message> messages) {}
}

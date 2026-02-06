package com.llmplayground.dm.infrastructure.llm;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class NoopLLMClient implements LLMClient {

    @Override
    public LLMResponse generate(LLMRequest request) {
        return new LLMResponse(
                "[milestone-0 placeholder narrative] Received input and awaiting tool integrations.",
                List.of());
    }
}

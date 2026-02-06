package com.llmplayground.dm.infrastructure.llm;

import java.util.List;

public interface LLMClient {
    LLMResponse generate(LLMRequest request);

    record LLMRequest(String model, List<PromptMessage> messages, List<ToolDefinition> tools) {}
    record PromptMessage(String role, String content) {}
    record ToolDefinition(String name, String description, String jsonSchema) {}
    record LLMResponse(String narrativeText, List<ToolInvocation> toolInvocations) {}
    record ToolInvocation(String toolName, String argumentsJson) {}
}

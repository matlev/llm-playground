package com.llmplayground.dm.tools;

import org.springframework.stereotype.Component;

@Component
public class NoopRulesEngine implements RulesEngine {

    @Override
    public RulesDecision evaluatePlayerAction(String actionText, long sessionId) {
        return new RulesDecision(false, "NONE", "N/A", "Rules integration pending milestone 1");
    }
}

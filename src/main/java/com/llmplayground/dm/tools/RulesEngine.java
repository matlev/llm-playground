package com.llmplayground.dm.tools;

public interface RulesEngine {
    RulesDecision evaluatePlayerAction(String actionText, long sessionId);

    record RulesDecision(boolean requiresCheck, String checkType, String dc, String note) {}
}

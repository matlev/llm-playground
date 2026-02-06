# Milestone 0 Architecture + Foundations

## Core principles
- **Code is the source of truth** for rules, dice, state transitions, and persistence.
- **LLM is coordinator/narrator**, not the rules adjudicator.
- **Deterministic + auditable randomness** via seeded dice tool (seed convention: `campaignId/sessionId/turn/purpose`) so replay runs produce the same outcomes.
- **Replayability** through durable message + tool-call logs and explicit event ordering (`sequence_index`, `correlation_id`).
- **Provider agnosticism** through an `LLMClient` port and provider adapters.

## Service/module boundaries

### 1) Orchestrator/Narration module
- Accept player input.
- Build LLM prompt context from durable session state and recent history.
- Expose tool definitions to LLM through `LLMClient`.
- Persist model outputs and proposed tool invocations.

### 2) Rules/Tools module
- Evaluate player actions, request checks, run encounter transitions, and roll dice.
- Register tools through `ToolRegistry`; execute via concrete tools in Milestone 1+.
- Return structured, machine-verifiable outputs.

### 3) State/Persistence module
- Postgres-backed entities for campaign/session/player/message/tool_call/encounter.
- Flyway migration controls schema evolution.
- Message and tool-call logs are first-class and include `session_id`, `sequence_index`, and `correlation_id` indexes.

### 4) Future evaluation harness
- Scenario replay runner will read message and tool-call history.
- Invariants (e.g., no unauthorized state mutation, deterministic dice consistency) validated per run.

## Data flow
1. Player sends message to `/api/sessions/{id}/messages`.
2. Message is persisted as `messages(role=PLAYER)` with sequence/correlation metadata.
3. Orchestrator calls `LLMClient.generate` with current tools from `ToolRegistry`.
4. Stub DM response is persisted as `messages(role=DM)` (Milestone 0).
5. Milestone 1+ will persist tool calls and execute rules engine transitions.

## LLM provider abstraction plan
- `LLMClient` interface defines a minimal contract:
  - request: model, prompt messages, tool definitions (JSON schema)
  - response: narrative text + tool invocations
- Provider-specific adapters (`OpenAiLLMClient`, `ClaudeLLMClient`, etc.) implement the interface.
- Spring wiring selects adapter by config without touching orchestrator logic.

## Milestone 0 success criteria checklist
- [x] Architecture doc matches code boundaries and principles.
- [x] Flyway-managed schema exists for campaigns/sessions/players/messages/tool_calls/encounters.
- [x] Indexes for `session_id + sequence_index` and `correlation_id` on event/tool logs.
- [x] Uniqueness constraints enforce one ordered event per `(session_id, sequence_index)` for messages/tool calls.
- [x] Service containerization for local Docker boot with Postgres.
- [x] Health endpoint available at `/actuator/health`.
- [x] CRUD skeleton for campaign/session/player and message posting.
- [x] Posting player message stores both player and stub DM message rows.
- [x] Provider swap requires only `LLMClient` adapter replacement.

# Milestone 0 Architecture + Foundations

## Core principles
- **Code is the source of truth** for rules, dice, state transitions, and persistence.
- **LLM is coordinator/narrator**, not the rules adjudicator.
- **Deterministic + auditable randomness** via seeded dice tool (seed convention: campaign/session/turn/purpose).
- **Replayability** through durable message + tool-call logs and explicit event ordering.
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
- Message and tool-call logs enable audit + replay.

### 4) Future evaluation harness
- Scenario replay runner will read message and tool-call history.
- Invariants (e.g., no unauthorized state mutation, deterministic dice consistency) validated per run.

## Data flow
1. Player sends message to `/api/sessions/{id}/messages`.
2. Message is persisted as `messages(role=PLAYER)`.
3. Orchestrator calls `LLMClient.generate` with current tools from `ToolRegistry`.
4. LLM output/tool proposals will be persisted in `messages(role=DM)` and `tool_calls` (full loop in Milestone 1).
5. Rules engine validates/executes tools, updates `encounters` + related state.

## LLM provider abstraction plan
- `LLMClient` interface defines a minimal contract:
  - request: model, prompt messages, tool definitions (JSON schema)
  - response: narrative text + tool invocations
- Provider-specific adapters (`OpenAiLLMClient`, `ClaudeLLMClient`, etc.) implement the interface.
- Spring wiring selects adapter by config without touching orchestrator logic.

## Milestone 0 success criteria mapping
- Architecture/doc + contracts: complete in `docs/architecture.md` and schema files.
- Persistence model: implemented via Flyway `V1__init.sql`.
- Skeleton API and domain stubs: compile and run with Spring Boot.
- Basic CRUD path: create campaign/session + post/fetch session state.

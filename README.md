# DM Service (Milestone 0 Skeleton)

Spring Boot foundation for a provider-agnostic, auditable DM-as-a-service system.

## What this milestone includes
- Architecture and principles doc: `docs/architecture.md`
- Tool-call JSON contract: `docs/tool-call-contract.schema.json`
- Flyway migration for core tables:
  - campaigns, sessions, players, messages, tool_calls, encounters
- Spring Boot skeleton with:
  - campaign/session/message API placeholders
  - `LLMClient` abstraction + `NoopLLMClient`
  - `ToolRegistry` + `RulesEngine` stubs
  - correlation-id request filter and logging pattern

## Quickstart
1. Start Postgres (or use Docker compose in a later milestone).
2. Set env vars (optional defaults shown):
   - `DB_URL=jdbc:postgresql://localhost:5432/dm_service`
   - `DB_USER=dm`
   - `DB_PASSWORD=dm`
3. Run:
   - `./mvnw spring-boot:run` (if wrapper added later), or
   - `mvn spring-boot:run`

## Placeholder endpoints
- `POST /api/campaigns`
- `GET /api/campaigns`
- `POST /api/sessions`
- `POST /api/sessions/{sessionId}/messages`
- `GET /api/sessions/{sessionId}/state`

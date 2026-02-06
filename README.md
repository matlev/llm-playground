# DM Service (Milestone 0 Skeleton)

Spring Boot foundation for a provider-agnostic, auditable DM-as-a-service system.

## What this milestone includes
- Architecture and principles doc: `docs/architecture.md`
- Tool-call JSON contract: `docs/tool-call-contract.schema.json`
- Flyway migration for core tables:
  - campaigns, sessions, players, messages, tool_calls, encounters
- Event log metadata and ordering guarantees for replay/debugging:
  - `sequence_index`, `correlation_id`
  - uniqueness on `(session_id, sequence_index)` for message/tool logs
- Spring Boot skeleton with:
  - campaign/session/player/message API placeholders
  - `LLMClient` abstraction + `NoopLLMClient`
  - `ToolRegistry` + `RulesEngine` stubs
  - correlation-id request filter and logging pattern
  - health endpoint (`/actuator/health`)
- Dockerized local runtime for app + Postgres

## Run with Docker
```bash
docker compose up --build
```

Health check:
```bash
curl http://localhost:8080/actuator/health
```

## Placeholder endpoints
- `POST /api/campaigns`
- `GET /api/campaigns`
- `POST /api/players`
- `POST /api/sessions`
- `POST /api/sessions/{sessionId}/messages`
- `GET /api/sessions/{sessionId}/state`

## Example CRUD flow
1. Create campaign (`POST /api/campaigns`).
2. Create player for campaign (`POST /api/players`).
3. Create session in campaign (`POST /api/sessions`).
4. Post player message (`POST /api/sessions/{id}/messages`).
5. Fetch state (`GET /api/sessions/{id}/state`) to see persisted player + stub DM messages.

## Provider swap guarantee
The orchestration and persistence layers depend only on `LLMClient`. To switch providers, implement a new adapter and wire it in Spring without changing controllers/services/entities.

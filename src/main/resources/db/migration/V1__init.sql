CREATE TABLE campaigns (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE sessions (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL REFERENCES campaigns(id),
    title VARCHAR(255) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE players (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL REFERENCES campaigns(id),
    name VARCHAR(255) NOT NULL,
    character_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES sessions(id),
    player_id BIGINT REFERENCES players(id),
    role VARCHAR(32) NOT NULL,
    content TEXT NOT NULL,
    sequence_index BIGINT NOT NULL,
    correlation_id VARCHAR(64) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE tool_calls (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES sessions(id),
    tool_name VARCHAR(255) NOT NULL,
    arguments_json TEXT NOT NULL,
    result_json TEXT,
    status VARCHAR(32) NOT NULL,
    sequence_index BIGINT NOT NULL,
    correlation_id VARCHAR(64) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE encounters (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES sessions(id),
    state_json TEXT NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE INDEX idx_sessions_campaign_id ON sessions(campaign_id);
CREATE INDEX idx_players_campaign_id ON players(campaign_id);
CREATE INDEX idx_messages_session_id_sequence_index ON messages(session_id, sequence_index);
CREATE INDEX idx_messages_correlation_id ON messages(correlation_id);
CREATE INDEX idx_tool_calls_session_id_sequence_index ON tool_calls(session_id, sequence_index);
CREATE INDEX idx_tool_calls_correlation_id ON tool_calls(correlation_id);
CREATE INDEX idx_encounters_session_id ON encounters(session_id);

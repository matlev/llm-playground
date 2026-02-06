ALTER TABLE messages
    ADD CONSTRAINT uq_messages_session_sequence UNIQUE (session_id, sequence_index);

ALTER TABLE tool_calls
    ADD CONSTRAINT uq_tool_calls_session_sequence UNIQUE (session_id, sequence_index);

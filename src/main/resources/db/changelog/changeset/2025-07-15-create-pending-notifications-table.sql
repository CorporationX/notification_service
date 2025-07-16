--liquibase changeset dextomi
CREATE TABLE pending_notifications
(
    id                BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    recipient_id      BIGINT      NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    status            VARCHAR(20) NOT NULL,
    created_at        TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    sent_at           TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    error_text        TEXT,
    retry_count       INT         DEFAULT 0
);
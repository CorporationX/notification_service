CREATE SCHEMA IF NOT EXISTS notifications;

CREATE TABLE IF NOT EXISTS notifications.events
(
    id           UUID PRIMARY KEY,
    processed_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

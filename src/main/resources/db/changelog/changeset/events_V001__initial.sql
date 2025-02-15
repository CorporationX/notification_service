CREATE SCHEMA IF NOT EXISTS notification_service;

CREATE TABLE IF NOT EXISTS notification_service.events
(
    id           UUID PRIMARY KEY,
    processed_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

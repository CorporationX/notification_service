CREATE TABLE notification_event_log (
    event_id BIGINT NOT NULL,
    event_type INT NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    PRIMARY KEY (event_type, event_id)
);
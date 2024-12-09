CREATE TABLE sms_messages (
    uid BIGINT PRIMARY KEY UNIQUE,
    content VARCHAR(4096),
    receiver_id BIGINT NOT NULL,
    delivery_status VARCHAR(64) NOT NULL DEFAULT 'IN_QUEUE',
    send_time TIMESTAMPTZ,
    received_time TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    cost DECIMAL(3, 3)
)
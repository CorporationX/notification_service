package faang.school.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO для события подписки/отписки пользователя на другого пользователя.
 * Содержит идентификаторы подписчика и подписанного пользователя,
 * а также временную метку события.
 */
@Data
public class FollowerEvent {
    private long followerId;
    private long followeeId;
    private LocalDateTime timestamp;
}
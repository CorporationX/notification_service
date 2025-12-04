package faang.school.notificationservice.dto.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestEventDto (
        UUID requestId,
        Long userId,
        OperationType operationType,
        RequestStatus status,
        LocalDateTime timestamp
) {
}
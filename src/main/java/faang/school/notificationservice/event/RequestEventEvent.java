package faang.school.notificationservice.event;

import faang.school.notificationservice.enums.RequestStatus;
import faang.school.notificationservice.enums.RequestType;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Builder
public record RequestEventEvent(
        @NonNull
        UUID id,
        long userId,
        @NonNull
        RequestType requestType,
        long blockValue,
        @NonNull
        Map<String, Object> body,
        RequestStatus requestStatus,
        String details,
        LocalDateTime timestamp
) {
}

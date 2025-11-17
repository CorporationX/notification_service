package faang.school.notificationservice.dto.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

/**
 * Domain event representing when one user starts following another.
 * Published to Kafka and consumed by notification-service.
 */
@Builder
public record NewFollowerEventDto(
        long actorId,
        long receiverId,
        String followerDisplayName
) {
    /**
     * Computed Kafka message key: actorId-receiverId-eventType.
     * Not part of the JSON payload.
     */
    @JsonIgnore
    public String getKey() {
        return Long.toString(receiverId);
    }
}
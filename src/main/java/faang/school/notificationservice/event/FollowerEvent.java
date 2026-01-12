package faang.school.notificationservice.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public record FollowerEvent(
        long followeeId,
        long followerId,
        LocalDateTime timestamp
) implements NotificationEvent {

    @Override
    @JsonIgnore
    public long getReceiverId() {
        return followeeId;
    }
}

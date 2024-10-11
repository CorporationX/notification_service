package faang.school.notificationservice.event;

import lombok.Builder;

import java.util.List;

@Builder
public record EventStartEvent(long id, List<Long> userIds) {
}

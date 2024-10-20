package faang.school.notificationservice.model.event;

import lombok.Builder;

import java.util.List;

@Builder
public record EventStartEvent(long id, List<Long> userIds) {
}

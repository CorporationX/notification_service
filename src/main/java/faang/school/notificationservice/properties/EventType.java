package faang.school.notificationservice.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    MENTORSHIP_ACCEPTED,
    PUBLISHED_POST,
    LIKED_POST,
    COMMENT_CREATED;
}

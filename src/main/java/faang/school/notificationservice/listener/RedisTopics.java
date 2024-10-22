package faang.school.notificationservice.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RedisTopics {
    ACHIEVEMENT_CHANNEL("achievement_channel"),
    LIKE_CHANNEL("like_channel");

    private final String topic;
}

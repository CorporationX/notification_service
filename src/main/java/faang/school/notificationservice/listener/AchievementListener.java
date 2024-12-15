package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.AchievementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementListener implements MessageListener {
    private final EventListenerHandler<AchievementEvent> eventListenerHandler;
    private final UserServiceClient userServiceClient;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        eventListenerHandler.eventHandler(
                message,
                AchievementEvent.class,
                event -> userServiceClient.getUser(event.getUserId())
        );
    }
}

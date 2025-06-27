package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.redis.RedisProperties;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> {

    private final List<String> topicNameKeys = List.of("goal-complete");
    private final Locale absolutelyCustomLocale = Locale.ENGLISH;

    public GoalCompletedEventListener(List<NotificationService> notificationServices,
                                      List<MessageBuilder<GoalCompletedEvent>> messageBuilders,
                                      UserServiceClient userServiceClient,
                                      ObjectMapper objectMapper,
                                      RedisProperties redisProperties) {
        super(notificationServices, messageBuilders, userServiceClient, objectMapper, redisProperties);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalCompletedEvent event = objectMapper.readValue(message.getBody(), GoalCompletedEvent.class);
            String generalizedNotification = getMessage(absolutelyCustomLocale, event);
            List<UserDto> usersCompletedGoal = userServiceClient.getUsersByIds(event.userIds());

            usersCompletedGoal.forEach(userDto -> {
                String personalNotification = generalizedNotification.formatted(userDto.getUsername());
                sendNotification(userDto, personalNotification);
            });

            log.debug("Notification(s) about goal {} completion were sent to users with ids: {}", event.goalTitle(), event.userIds());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    protected List<String> getTopicNameKeys() {
        return topicNameKeys;
    }
}

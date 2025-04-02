package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.GoalCompletedEvent;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> {

    private final ObjectMapper objectMapper;

    public GoalCompletedEventListener(ObjectMapper objectMapper,
                                      UserServiceClient userServiceClient,
                                      Map<UserNotificationDto.PreferredContact,
                                              NotificationService> notificationServicesMap,
                                      Map<Class<?>, MessageBuilder<?>> messageBuildersMap) {
        super(userServiceClient, notificationServicesMap, messageBuildersMap);
        this.objectMapper = objectMapper;
    }

    public void handleMessage(String json) {
        try {
            GoalCompletedEvent event = objectMapper.readValue(json, GoalCompletedEvent.class);
            log.info("Received event from GoalCompletedEvent: {}", event);

            UserNotificationDto userDto = userServiceClient.getUserNotificationDto(event.getUserId());
            String message = super.getMessage(event, userDto.getLocale());
            super.sendNotification(event.getUserId(), message);

        } catch (Exception e) {
            log.error("Processing error from GoalCompletedEvent: {}", json, e);
        }
    }
}
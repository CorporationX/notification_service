package faang.school.notificationservice.listener.goal;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.goal.GoalCompletedEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> {

    public GoalCompletedEventListener(ObjectMapper objectMapper,
                                      UserServiceClient userServiceClient,
                                      Map<Class<?>, MessageBuilder<?>> messageBuilders,
                                      Map<UserDto.PreferredContact, NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Got message, trying to handle it");
        handleEvent(message, GoalCompletedEvent.class, event -> {
            UserDto user = userServiceClient.getUser(event.getUserId());
            Locale userPreferedLocale = user.getLocale() != null ? user.getLocale() : Locale.ENGLISH;
            String text = getMessage(event, userPreferedLocale);
            sendNotification(user, text);
        });
    }
}

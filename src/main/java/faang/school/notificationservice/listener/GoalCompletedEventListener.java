package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.GoalCompletedEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEventDto> implements MessageListener {

    public GoalCompletedEventListener(ObjectMapper objectMapper, UserServiceClient userService
            , List<NotificationService> services, MessageBuilder<GoalCompletedEventDto> messageBuilder) {
        super(objectMapper, userService, services, messageBuilder);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        GoalCompletedEventDto goalCompletedEventDto = handleEvent(message, GoalCompletedEventDto.class);

        String text = getMessage(goalCompletedEventDto, Locale.ENGLISH);

        sendNotification(goalCompletedEventDto.userId(), text);
    }
}
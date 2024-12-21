package faang.school.notificationservice.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompletedEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEventDto> implements MessageListener {
    public GoalCompletedEventListener(ObjectMapper objectMapper,
                                      UserServiceClient userServiceClient,
                                      List<NotificationService> notificationService,
                                      List<MessageBuilder<GoalCompletedEventDto>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationService, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(GoalCompletedEventDto.class, message, event -> {
            String messageText = getMessage(event, Locale.getDefault());
            log.info("Received a message text from Redis channel: {}", messageText);
            sendNotification(event.getUserId(), messageText);
        });
    }
}



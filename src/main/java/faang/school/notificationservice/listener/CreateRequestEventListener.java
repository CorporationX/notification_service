package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CreateRequestEvent;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class CreateRequestEventListener extends AbstractEventListener<CreateRequestEvent> implements MessageListener {

    public CreateRequestEventListener(UserServiceClient userServiceClient, List<NotificationService> notificationServiceList, List<MessageBuilder<CreateRequestEvent>> messageBuilders, ObjectMapper objectMapper) {
        super(userServiceClient, notificationServiceList, messageBuilders, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, CreateRequestEvent.class, createRequestEvent -> {
            String text = getMessage(createRequestEvent, Locale.UK);
            sendNotification(createRequestEvent.getUserId(), text);
        });
    }
}
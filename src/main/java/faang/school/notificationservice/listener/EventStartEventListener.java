package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventStartEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class EventStartEventListener extends AbstractEventListener<EventStartEvent> implements MessageListener {

    public EventStartEventListener(ObjectMapper objectMapper,
                                   UserServiceClient userServiceClient,
                                   List<MessageBuilder<EventStartEvent>> messageBuilders,
                                   List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            EventStartEvent event = objectMapper.readValue(message.getBody(), EventStartEvent.class);
            sendNotification(event.getSubscriberId(), buildMessage(event, Locale.UK));
            log.info("The event {} has been successfully sent", event.getTitle());
        } catch (IOException e) {
            log.error("The event was delivered incorrectly");
            throw new RuntimeException(e);
        }
    }
}

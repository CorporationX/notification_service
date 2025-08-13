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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

@Component
@Slf4j
public class EventStartListener extends AbstractEventListener<EventStartEvent> implements MessageListener {

    public EventStartListener(ObjectMapper objectMapper,
                              UserServiceClient userServiceClient,
                              List<NotificationService> notificationServices,
                              List<MessageBuilder<EventStartEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageStr = new String(message.getBody(), StandardCharsets.UTF_8);
            log.info(messageStr);
            EventStartEvent event = objectMapper.readValue(messageStr, EventStartEvent.class);
            List<Long> attendeesIds = event.getAttendeesIds();
            String notificationText = getMessage(event, Locale.UK);
            attendeesIds.forEach(attendeeId -> sendNotification(attendeeId, notificationText));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при десериализации сообщения", e);
        }
    }
}

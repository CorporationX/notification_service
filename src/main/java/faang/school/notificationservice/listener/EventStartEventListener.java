package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.event.EventStartEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class EventStartEventListener extends AbstractEventListener<EventStartEvent> implements MessageListener {

    public EventStartEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                   List<MessageBuilder<EventStartEvent>> messageBuilders,
                                   List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, EventStartEvent.class, eventStartEvent -> {
            List<Long> attendeesIds = eventStartEvent.getAttendeesIds();
            List<UserContactsDto> users = attendeesIds.stream().map(userServiceClient::getUserContacts).toList();
            users.forEach(user -> sendNotification(user.getId(), getMessage(eventStartEvent, Locale.getDefault())));
            log.info("Event start event received: {}", eventStartEvent);
        });
    }
}

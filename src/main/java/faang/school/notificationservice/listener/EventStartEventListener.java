package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventDto;
import faang.school.notificationservice.dto.EventStartEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.util.TimeUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Component
public class EventStartEventListener extends AbstractEventListener<EventStartEvent> implements MessageListener {

    public EventStartEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient, List<NotificationService> notificationServiceList, List<MessageBuilder<EventStartEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServiceList, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, EventStartEvent.class, eventStartEvent -> {
            EventDto eventDto = userServiceClient.getEvent(eventStartEvent.getEventId());

            LocalDateTime startDate = eventDto.getStartDate();
            LocalDateTime now = LocalDateTime.now();
            Duration timeToEvent = Duration.between(now, startDate);

            String text = getMessage(eventStartEvent,
                    Locale.UK, new Object[]{eventDto.getTitle(), TimeUtils.formatTimeToEvent(timeToEvent)});

            sendNotificationToUsers(eventStartEvent.getParticipants(), text);
        });
    }
}

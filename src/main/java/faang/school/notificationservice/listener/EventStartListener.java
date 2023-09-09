package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.EventStartDto;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messageBuilder.MessageBuilder;
import faang.school.notificationservice.sender.NotificationService;
import faang.school.notificationservice.service.EventStartService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EventStartListener extends AbstractEventListener<EventStartDto, String> implements MessageListener {
    private final EventStartService eventStartService;

    public EventStartListener(ObjectMapper objectMapper, Map<UserDto.PreferredContact, NotificationService> notifications, Map<Class<?>, MessageBuilder> messageBuilders, UserServiceClient userServiceClient
            , EventStartService eventStartService) {
        super(objectMapper, notifications, messageBuilders, userServiceClient);
        this.eventStartService = eventStartService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = getMessageBody(message);
        EventStartDto eventStartDto = getEvent(messageBody, EventStartDto.class);
        eventStartService.scheduleNotifications(eventStartDto);
    }
}

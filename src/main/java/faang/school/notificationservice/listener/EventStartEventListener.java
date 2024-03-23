package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.EventStartEventDto;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventStartEventListener extends AbstractEventListener<EventStartEventDto> implements MessageListener {


    public EventStartEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                   List<MessageBuilder<EventStartEventDto>> messageBuilders,
                                   List<NotificationService> services) {
        super(objectMapper, userServiceClient, messageBuilders, services);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, EventStartEventDto.class, (eventDto) -> {
            eventDto.getAttendeesId().forEach(id -> sendMessage(eventDto, id));
        });
    }
}

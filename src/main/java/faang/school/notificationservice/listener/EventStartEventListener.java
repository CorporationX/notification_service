package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.EventStartEventDto;
import faang.school.notificationservice.mapper.JsonObjectMapper;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class EventStartEventListener extends AbstractEventListener<EventStartEventDto> implements MessageListener {


    public EventStartEventListener(UserServiceClient userService,
                                   JsonObjectMapper jsonObjectMapper,
                                   List<NotificationService> services,
                                   List<MessageBuilder<EventStartEventDto>> messageBuilders) {
        super(userService, jsonObjectMapper, services, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, EventStartEventDto.class, (eventDto) -> {
            eventDto.getUserIds().forEach(id -> sendMessage(eventDto, id, id));
        });
    }
}
package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class EventStartListener extends AbstractEventListener<EventDto> implements MessageListener {

    public EventStartListener(ObjectMapper objectMapper,
                              UserServiceClient userServiceClient,
                              List<NotificationService> notificationServices,
                              List<MessageBuilder<EventDto>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            EventDto event = objectMapper.readValue(message.getBody(), EventDto.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

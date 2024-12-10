package faang.school.notificationservice.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventStartEventDto;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Component
public class EventStartEventListener extends AbstractEventListener<EventStartEventDto> {

    @Value("${spring.data.redis.channel.event_start}")
    private String eventStartTopic;

    public EventStartEventListener(ObjectMapper objectMapper,
                                   UserServiceClient userServiceClient,
                                   List<NotificationService> notificationService,
                                   List<MessageBuilder<EventStartEventDto>> messageBuilder) {
        super(objectMapper, userServiceClient, notificationService, messageBuilder);
    }

    private final Consumer<EventStartEventDto> sendNotifications = eventStartEventDto ->
            eventStartEventDto.getEventParticipants()
                    .forEach(userId -> sendNotification(userId, getMessage(eventStartEventDto, Locale.ENGLISH)));

    @Override
    public void onMessage(Message message, byte[] pattern) {
        processEvent(message, EventStartEventDto.class, sendNotifications);
    }

    @Override
    public Topic getTopic() {
        return new ChannelTopic(eventStartTopic);
    }
}
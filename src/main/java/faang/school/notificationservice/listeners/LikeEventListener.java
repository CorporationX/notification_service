package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.events.LikeEvent;
import faang.school.notificationservice.listeners.general.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;

import java.util.List;
import java.util.Locale;

@Configuration
public class LikeEventListener extends AbstractEventListener<LikeEvent> {


    public LikeEventListener(ObjectMapper objectMapper,
                             UserServiceClient userServiceClient,
                             List<NotificationService> notificationServices,
                             List<MessageBuilder<LikeEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        LikeEvent event = constructEvent(message.getBody(), LikeEvent.class);
        sendMessage(event, event.getAuthorId(), Locale.ENGLISH);
    }
}

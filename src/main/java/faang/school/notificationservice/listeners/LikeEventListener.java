package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.events.LikeEvent;
import faang.school.notificationservice.listeners.general.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LikeEventListener extends AbstractEventListener<LikeEvent> {


    public LikeEventListener(ObjectMapper objectMapper,
                             UserServiceClient userServiceClient,
                             List<NotificationService> notificationServices,
                             List<MessageBuilder<LikeEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    protected Class<LikeEvent> getEventClassType() {
        return LikeEvent.class;
    }
}

package faang.school.notificationservice.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.message.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.message.event.ProfileViewEvent;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> implements MessageListener {

    protected ProfileViewEventListener(ObjectMapper mapper,
                                       UserServiceClient userServiceClient,
                                       List<NotificationService> notificationServices,
                                       MessageBuilder<ProfileViewEvent> messageBuilder) {
        super(mapper, userServiceClient, notificationServices, messageBuilder);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfileViewEvent.class);
    }
}

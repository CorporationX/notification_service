package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.messaging.events.ProfileViewEvent;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> {


    public ProfileViewEventListener(ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    List<NotificationService> notificationServices,
                                    List<MessageBuilder<Class<?>>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEvent profileViewEvent = convertToJSON(message, ProfileViewEvent.class);
        String message2 = getMessage(profileViewEvent.getClass(), Locale.ENGLISH);
        sendNotification(profileViewEvent.getIdVisited(), message2);
    }
}

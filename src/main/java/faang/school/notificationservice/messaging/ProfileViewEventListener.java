package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.massages.MessageBuilder;
import faang.school.notificationservice.messaging.events.ProfileViewEvent;
import faang.school.notificationservice.service.AbstractEventListener;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Service
public class ProfileViewEventListener<T> extends AbstractEventListener implements MessageListener {

    @Autowired
    public ProfileViewEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                    List<NotificationService> notificationServices,
                                    List<MessageBuilder<T>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEvent profileViewEvent = getEvent(message);
        sendNotification(profileViewEvent.getIdVisited(), getMessage(profileViewEvent, Locale.ENGLISH));
    }

    private ProfileViewEvent getEvent(Message message) {
        ProfileViewEvent profileViewEvent;
        try {
            profileViewEvent = objectMapper.readValue(message.getBody(), ProfileViewEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return profileViewEvent;
    }

}

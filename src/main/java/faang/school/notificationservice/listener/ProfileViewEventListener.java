package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.profile.ProfileViewEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


@Slf4j
@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> implements MessageListener {

    public ProfileViewEventListener(ObjectMapper objectMapper, List<MessageBuilder<ProfileViewEvent>> messageBuilders, UserServiceClient userServiceClient, List<NotificationService> notificationServices) {
        super(objectMapper, messageBuilders, userServiceClient, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ProfileViewEvent event = objectMapper.readValue(message.getBody(), ProfileViewEvent.class);
            String text = getMessage(event,Locale.getDefault());
            sendNotification(event.getAuthorId(), text);
            log.info("Successfully sent a message to (userId: {})", event.getAuthorId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
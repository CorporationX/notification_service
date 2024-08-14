package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> implements MessageListener {


    public ProfileViewEventListener(ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    List<NotificationService> notificationServices,
                                    List<MessageBuilder<ProfileViewEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ProfileViewEvent event = objectMapper.readValue(message.getBody(), ProfileViewEvent.class);
            UserDto viewer = userServiceClient.getUser(event.getViewerId());
            String messageText = viewer.getUsername() + getMessage(event, Locale.UK);

            sentNotification(event.getUserId(), messageText);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
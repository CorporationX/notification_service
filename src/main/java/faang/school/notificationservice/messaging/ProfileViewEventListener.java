package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Locale;

@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEvent> implements MessageListener {

    public ProfileViewEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient, List<MessageBuilder<ProfileViewEvent>> messageBuilders, List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfileViewEvent.class, (event) -> {
            UserDto viewer = userServiceClient.getUser(event.getProfileOwnerId());
            String text = getMessage(event);
            sendNotification(event.getProfileOwnerId(), text);
        });
    }
}

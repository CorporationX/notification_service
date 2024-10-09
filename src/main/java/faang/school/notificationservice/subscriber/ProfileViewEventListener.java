package faang.school.notificationservice.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.ProfileViewEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEventDto> implements MessageListener {
    Logger logger = LoggerFactory.getLogger(ProfileViewEventListener.class);

    public ProfileViewEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            MessageBuilder<ProfileViewEventDto> messageBuilder,
            List<NotificationService> notificationServices) {
        super(
                objectMapper,
                userServiceClient,
                messageBuilder,
                notificationServices
        );
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProfileViewEventDto profileViewEvent = handleEvent(message, ProfileViewEventDto.class);
        String text = getMessage(profileViewEvent, Locale.getDefault());
        sendNotification(profileViewEvent.getReceiverId(), text);
    }
}

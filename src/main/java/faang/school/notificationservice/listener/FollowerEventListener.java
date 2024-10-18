package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.FollowerEventMessageBuilder;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;
import java.util.List;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

@Component("followerEventListener")
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {
    private final static Locale DEFAULT_LOCALE = Locale.ENGLISH;

    public FollowerEventListener(List<NotificationService> notificationServices,
                                 List<MessageBuilder<FollowerEvent>> messageBuilders,
                                 UserServiceClient userServiceClient,
                                 ObjectMapper objectMapper) {
        super(notificationServices, messageBuilders, userServiceClient, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEvent followerEvent = handleEvent(message, FollowerEvent.class);
        String messageText = getMessage(followerEvent, DEFAULT_LOCALE);
        sendNotification(followerEvent.followeeId(), messageText);
    }
}

package faang.school.notificationservice.listener.follower;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.event.follower.FollowerEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
public class FollowerMessageListener extends AbstractEventListener<FollowerEvent> {

    public FollowerMessageListener(ObjectMapper objectMapper,
                                   UserServiceClient userServiceClient,
                                   Map<Class<?>, MessageBuilder<?>> messageBuilders,
                                   Map<UserDto.PreferredContact, NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Got message, trying to handle it");
        handleEvent(message, FollowerEvent.class, event -> {
            UserDto followeeUser = userServiceClient.getUser(event.getFolloweeId());
            Locale userPreferedLocale = followeeUser.getLocale() != null ? followeeUser.getLocale() : Locale.ENGLISH;
            String text = getMessage(event, userPreferedLocale);

            sendNotification(followeeUser, text);
        });
    }
}

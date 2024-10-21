package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.dto.event.LikePostEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component
public class LikePostEventListener extends AbstractEventListener<LikePostEvent> {

    public LikePostEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 Map<Class<?>, MessageBuilder<?>> messageBuilders,
                                 Map<UserDto.PreferredContact, NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, LikePostEvent.class, likePostEvent -> {
            UserDto postAuthor = userServiceClient.getUser(likePostEvent.getPostAuthorId());
            Locale userPreferedLocale =
                    postAuthor.getLocale() != null ? postAuthor.getLocale() : Locale.ENGLISH;
            String text = getMessage(likePostEvent, userPreferedLocale);
            sendNotification(postAuthor, text);
        });
    }
}

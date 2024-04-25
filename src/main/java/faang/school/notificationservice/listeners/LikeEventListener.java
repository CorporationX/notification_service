package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class LikeEventListener extends AbstractListener<LikeEvent> {

    public LikeEventListener(ObjectMapper objectMapper,
                             UserServiceClient userServiceClient,
                             List<NotificationService> notificationServicesList,
                             List<MessageBuilder<Class<?>>> messageBuildersList) {
        super(objectMapper, userServiceClient, notificationServicesList, messageBuildersList);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, LikeEvent.class, likeEvent -> {
            String textMessage = getMessage(likeEvent.getClass(), Locale.UK);
            sendNotification(likeEvent.getAuthorLikeId(), textMessage);
        });
    }
}
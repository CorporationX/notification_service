package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikePostEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class LikeEventListener extends AbstractEventListener<LikePostEvent> implements MessageListener {
    private final String LOG_EVENT_RECEIVED = "LikePostEvent event is received: {}";

    public LikeEventListener(List<NotificationService> notificationServices,
                             List<MessageBuilder<LikePostEvent>> messageBuilders,
                             UserServiceClient userServiceClient,
                             ObjectMapper objectMapper) {
        super(notificationServices, messageBuilders, userServiceClient, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            LikePostEvent likePostEvent = objectMapper.readValue(message.getBody(), LikePostEvent.class);
            log.info(LOG_EVENT_RECEIVED, likePostEvent.toString());
            String builtMessage = getMessage(likePostEvent, LocaleContextHolder.getLocale());
            sendNotification(likePostEvent.getPostAuthorId(), builtMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

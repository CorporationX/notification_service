package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> implements MessageListener {

    @Autowired
    public FollowerEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient, List<MessageBuilder<FollowerEvent>> messageBuilders, List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEvent event;
        try {
            event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
        } catch (IOException e) {
            log.error("Error reading FollowerEvent from message body: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        String text = getMessage(event, Locale.getDefault());

        sendNotification(event.getFolloweeId(), text);
    }
}

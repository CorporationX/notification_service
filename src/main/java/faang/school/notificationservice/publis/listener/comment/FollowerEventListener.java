package faang.school.notificationservice.publis.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.follower.FollowerEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.publis.listener.AbstractEventListener;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class FollowerEventListener extends AbstractEventListener<FollowerEventDto> implements MessageListener {
    public FollowerEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<MessageBuilder<FollowerEventDto>> messageBuilders,
            List<NotificationService> notificationServices
    ) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        log.info("Received messageBody: {}", messageBody);
        FollowerEventDto followerEvent = mapToEvent(messageBody, FollowerEventDto.class);

        String notificationMessage = getMessage(followerEvent, Locale.getDefault());
        log.info("Successful message receipt.");

        sendNotification(followerEvent.getFollowerId(), notificationMessage);
        log.info("Successful notification sending.");
    }
}

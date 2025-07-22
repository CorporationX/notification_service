package faang.school.notificationservice.listener.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.FollowerEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEventDto> implements MessageListener {

    public FollowerEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<FollowerEventDto>> messageBuilders, Utils utils) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders, utils);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("Received message from Redis: {}", messageBody);

        try {
            FollowerEventDto event = objectMapper.readValue(messageBody, FollowerEventDto.class);
            log.info("Parsed FollowerEventDto: {}", event);

            UserDto followee = userServiceClient.getUser(event.getFolloweeId());
            UserDto follower = userServiceClient.getUser(event.getFollowerId());

            if (followee != null && follower != null) {
                Locale locale = new Locale(followee.getPreference().toString().toLowerCase());
                String notificationMessage = getMessage(event, locale);

                log.info("Sending notification to user {}: {}", followee.getId(), notificationMessage);
                sendNotification(followee, notificationMessage);
            } else {
                if (followee == null) {
                    log.warn("Followee with id {} not found", event.getFolloweeId());
                }
                if (follower == null) {
                    log.warn("Follower with id {} not found", event.getFollowerId());
                }
            }

        } catch (IOException e) {
            log.error("Error processing message from Redis: {}", e.getMessage(), e);
            logError(e);
        }
    }

    void logError(Exception e) {
        log.error("An error occurred in FollowerEventListener: ", e);
    }
}
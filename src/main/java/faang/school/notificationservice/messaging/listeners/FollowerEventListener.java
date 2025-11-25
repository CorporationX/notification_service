package faang.school.notificationservice.messaging.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.message_builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Example listener for follower events using Redis Pub/Sub.
 * Demonstrates how to extend AbstractEventListener for different event sources.
 */
@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener implements MessageListener {

    public FollowerEventListener(
            ObjectMapper objectMapper,
            UserService userService,
            List<NotificationService> notificationServices,
            List<MessageBuilder<?>> messageBuilders) {
        super(objectMapper, userService, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // Parse the message - assuming it's a JSON with follower event data
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);

            log.info("Received follower event: {}", event);

            // Get the user who was followed
            UserDto followedUser = userService.getUser(event.followedUserId());

            // Build the notification message
            String notificationMessage = getMessage(
                    event,
                    FollowerEvent.class,
                    followedUser.getLocale()
            );

            // Send the notification
            sendNotification(event.followedUserId(), notificationMessage);

            log.info("Successfully sent follower notification to user {}", event.followedUserId());

        } catch (Exception e) {
            log.error("Error processing follower event", e);
        }
    }

    /**
     * Example follower event record.
     */
    public record FollowerEvent(Long followerId, Long followedUserId) {
    }
}
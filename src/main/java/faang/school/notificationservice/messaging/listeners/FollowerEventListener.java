package faang.school.notificationservice.messaging.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.message_builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import faang.school.notificationservice.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener {

    public FollowerEventListener(
            ObjectMapper objectMapper,
            UserService userService,
            List<NotificationService> notificationServices,
            List<MessageBuilder<?>> messageBuilders) {
        super(objectMapper, userService, notificationServices, messageBuilders);
    }

    @KafkaListener(topics = "${kafka.topics.follower}",
        containerFactory = "followerEventKafkaListenerContainerFactory",
        groupId = "notification-service-group")
    public void onMessage(FollowerEvent followerEvent) {
        try {
            log.info("Received follower event: {}", followerEvent);

            // Get the user who was followed
            UserDto followedUser = userService.getUser(followerEvent.followeeId());

            // Build the notification message
            String notificationMessage = getMessage(
                    followerEvent,
                    FollowerEvent.class,
                    followedUser.getLocale()
            );

            // Send the notification
            sendNotification(followerEvent.followeeId(), notificationMessage);

            log.info("Successfully sent follower notification to user {}", followerEvent.followeeId());

        } catch (Exception e) {
            log.error("Error processing follower event", e);
        }
    }
}
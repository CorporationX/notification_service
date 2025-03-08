package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserProfileDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.NotificationServiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowerEventListener {
    private final NotificationServiceFactory serviceFactory;
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<FollowerEvent> messageBuilder;
    private final RetryTemplate retryTemplate;

    @KafkaListener(topics = "${spring.kafka.topics.follower-events}")
    public void listen(FollowerEvent event) {
        try {
            validateEvent(event);
            UserProfileDto followee = retryTemplate.execute(ctx -> userServiceClient.getUserProfile(event.getFolloweeId()));

            if (followee.getPreference() == null) {
                log.warn("User {} has no preferred contact method", followee.getId());
                return;
            }

            String message = messageBuilder.buildMessage(event, Locale.getDefault());
            NotificationService service = serviceFactory.getService(followee.getPreference());
            service.send(followee, message);

            log.info("Notification sent via {} to user {}", followee.getPreference(), followee.getId());
        } catch (Exception e) {
            log.error("Error processing event: {}", e.getMessage());
        }
    }

    private void validateEvent(FollowerEvent event) {
        if (event.getFolloweeId() == null || event.getFollowerId() == null) {
            throw new IllegalArgumentException("Invalid FollowerEvent data");
        }
    }
}
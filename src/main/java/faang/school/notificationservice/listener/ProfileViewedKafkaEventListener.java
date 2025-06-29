package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewedEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class ProfileViewedKafkaEventListener extends AbstractKafkaEventListener<ProfileViewedEventDto> {

    public ProfileViewedKafkaEventListener(List<NotificationService> notificationServiceList,
                                           List<MessageBuilder<ProfileViewedEventDto>> builderList,
                                           UserServiceClient userServiceClient
    ) {
        super(notificationServiceList, builderList, userServiceClient);
    }

    @KafkaListener(
            topics = "user.profile.viewed",
            groupId = "notification-service.profile-view",
            containerFactory = "profileViewedKafkaListenerContainerFactory"
    )
    public void handleProfileViewedEvent(@Payload ProfileViewedEventDto event) {
        try {
            log.info("Received profile view event: {}", event);
            String message = getMessage(Locale.ENGLISH, event);
            sendNotification(event.getViewedId(), message);
        } catch (Exception ex) {
            log.error("Failed to process profile viewed event: {}", event, ex);
        }
    }
}

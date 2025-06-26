package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class KafkaProfileViewEventListener extends AbstractEventListener<ProfileViewEventDto> {

    public KafkaProfileViewEventListener(UserServiceClient userServiceClient,
                                         List<NotificationService> notificationServices,
                                         List<MessageBuilder<ProfileViewEventDto>> messageBuilders) {
        super(userServiceClient, notificationServices, messageBuilders);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.profile-view}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "profileViewKafkaListenerContainerFactory"
    )
    public void listen(ProfileViewEventDto event, Acknowledgment acknowledgment) {
        try {
            String localizedMessage = getMessage(event, Locale.JAPAN);
            sendNotification(event.getProfileOwnerId(), localizedMessage);
            acknowledgment.acknowledge();
        } catch (Exception exception) {
            log.warn("Failed to notify user {} about profile view by user {}",
                    event.getProfileOwnerId(), event.getViewerId(), exception);
        }
    }
}

package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class ProfileViewEventListener extends AbstractEventListener<ProfileViewEventDto> {

    public ProfileViewEventListener(UserServiceClient userServiceClient,
                                    List<NotificationService> notificationServices,
                                    List<MessageBuilder<ProfileViewEventDto>> messageBuilders) {
        super(userServiceClient, notificationServices, messageBuilders);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.profile-view}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "profileViewKafkaListenerContainerFactory"
    )
    public void listen(ProfileViewEventDto event) {
        log.info("====================asdasdasd====================");
        String localizedMessage = getMessage(event, Locale.JAPAN);
        sendNotification(event.getProfileOwnerId(), localizedMessage);
    }
}

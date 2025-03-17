package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.LikeEvent;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class LikeEventListener extends AbstractEventListener<LikeEvent> {
    public LikeEventListener(UserServiceClient userServiceClient,
                             Map<UserNotificationDto.PreferredContact, NotificationService> notificationServicesMap,
                             Map<Class<?>, MessageBuilder<?>> messageBuildersMap) {
        super(userServiceClient, notificationServicesMap, messageBuildersMap);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.like.name}",
            properties = {
                    "spring.json.value.default.type=${spring.kafka.topics.like.type}",
            }
    )
    public void onMessage(LikeEvent data) {
        log.info("\nReceived message from like topic kafka");
        UserNotificationDto userDto = userServiceClient.getUserNotificationDto(data.getAuthorId());
        String message = super.getMessage(data, userDto.getLocale());
        super.sendNotification(data.getAuthorId(), message);
    }

}

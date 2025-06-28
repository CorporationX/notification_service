package faang.school.notificationservice.listener;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementEventListener {
    private final NotificationService notificationService;
    private final MessageBuilder<AchievementEvent> messageBuilder;
    private final UserServiceClient userServiceClient;

    @KafkaListener(
            topics = "${spring.kafka.topics.achievement}",
            groupId = "notification-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(AchievementEvent event) {
        UserDto user = userServiceClient.getUser(event.getUserId());
        String message = messageBuilder.buildMessage(event, user.getLocale());
        notificationService.send(user, message);
    }
}